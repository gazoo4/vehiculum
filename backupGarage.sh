#!/usr/bin/env bash

###############################################################################
#
# backupGarage.sh      A script that creates a backup of the garage data from
#                      the phone to PC
#
###############################################################################

### CONFIGURATION START #######################################################
#
#

ENVIRONMENT_TEMPLATE="template-backup.env"
ENVIRONMENT_FILE="backup.env"

#
#
### CONFIGURATION END #########################################################

# ERROR CODES:
readonly ERR_DID_NOTHING=1
readonly ERR_PREREQ_MISSING=10
readonly ERR_CMD_FAILED=20
readonly ERR_INTERNAL=250

#
# Method for printing error messages to STDERR
# Input: $1: error message (string)
#
err() {
	local red='\033[0;31m'
	local no_color='\033[0m'
	echo -e "${red}$(date -u +'%F %T') $@${no_color}" >&2
}

#
# Method for printing colorized output to stdout
# Input: $@: Message to be printed
#
info() {
	local green='\033[0;32m'
	local no_color='\033[0m'
	echo -e "${green}$(date -u +'%F %T') $@${no_color}"
}


#
# Method for printing error message and exitting the script
# Input: $1: exit code (optional)
#        $2: error message (string)
#
complain() {
	local message
	local code

	# Only the message has been supplied (without the exit code)
	if [[ $# -eq 1 ]]; then
		# Add the default exit code
		code=${ERR_DID_NOTHING}
		message="$1"
	else
		code="$1"
		message="$2"
	fi  

	err "${message}"
	exit ${code}
}

#
# Method to execute a command and exit the script if the command fails
# Input: $@: Command to be executed (with args.)
#
try() {
	"$@"
	local status=$?
	if [ $status -ne 0 ]; then
		echo "Exitting due to error ${status} with $1" >&2
		exit $status
	fi
	return $status
}

#
# Method responsible for checking the pre-requisites
# If it finds a missing prereq, the script prints a msg to stdout and terminates the execution
#
checkPreReqs() {
	if [[ ! -f "${ENVIRONMENT_FILE}" ]]; then
		info "Environment configuration not found, creating one from a template"
		cp "${ENVIRONMENT_TEMPLATE}" "${ENVIRONMENT_FILE}"
	fi

	source "${ENVIRONMENT_FILE}"

	if [[ -z "${ADB}" ]]; then
		adb="${ADB}"
	else
		adb="$(which adb)"
	fi

	if [[ ! -f "${adb}" ]]; then
		complain ${ERR_PREREQ_MISSING} "Unable to find adb tool"
	fi

	info "Environment configuration has been loaded"

	if [[ "$(${adb} devices | wc -l)" -le "2" ]]; then
		complain "No device has been detected by the adb"
	fi
}

#
# Method to print the message about usage of the script
#
usage() {
	echo "Script for something..."
	echo ""
	echo "Usage: $0 [--help]"
	echo ""
	echo "    --help                      Prints this message"
	echo "    --destination               Override the destination folder"
	echo "    --force-overwrite           Overwrite the destination folder if it exists"
	echo ""
	echo "Example: $0"
	exit 0
}

forceOverwrite=false
#
# Method for extracting and validating guid from the arguments
# At the end of a successfull run it stores the GUID value in the guid variable
#
parseArgs() {
	local nextIsDestination=false

	for arg in "$@"; do
		if ${nextIsDestination}; then
			destination="${arg}"
			nextIsDestination=false
		elif [[ ${arg} =~ ^--help$ ]]; then
			usage
		elif [[ ${arg} =~ ^--destination$ ]]; then
			nextIsDestination=true
		elif [[ ${arg} =~ ^--force-overwrite$ ]]; then
			forceOverwrite=true
		else
			complain ${ERR_DID_NOTHING} "Unknown parameter: ${arg}"
		fi
	done

	if [[ -z "${destination}" ]]; then
		destination="${BACKUP_DESTINATION}"
	fi
	destination="${destination/#\~/$HOME}"
}

#
# Method responsible for executing the backup itself
#
runBackup() {
	today="$(date +%F)"
	destination="${destination}/${today}"

	if [[ -d "${destination}" ]]; then
		if ${forceOverwrite}; then
			info "Destination folder already exists, but forcing overwrite"
			rm -rf "${destination}"
		else
			complain "Destination folder ${destination} already exists. If you want to overwrite it, use --force-overwrite"
		fi
	fi

	${adb} shell "su -c \"rm -rf /sdcard/Download/garage; mkdir -p /sdcard/Download/garage && cp /data/data/sk.berops.android.fueller/garage* /sdcard/Download/garage && chmod -R a+r /sdcard/Download/garage\""
	mkdir -p "${destination}"
	${adb} pull -p "/sdcard/Download/garage" "${destination}"
}

#
# Main script body starts here
#

# Check if all the pre-requisites have been met and all the variables are set
checkPreReqs

# Read all the arguments and validate them
parseArgs "$@"

# Execute
runBackup

exit 0
