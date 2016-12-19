#!/bin/bash
# Ugly tool to trigger automatic conversion from *.svg images to all sizes of *.png images
# TODO: to be tested on Linux
#

#
# Environment settings
#
inkscape_bin_linux='/usr/bin/inkscape'

# Detect system
system=`uname -a | awk '{print $1}'`

#
# This method converts the svg to png using the predefined paths and using the correct density
# Parameter1: input svg filename
#             string
# Parameter2: screen density
#             enum: ldpi, mdpi, hdpi, xhdpi, xxhdpi, xxxhdpi
# Parameter3: dpi
#             integer
#
function convert {
	in_filename=$1
	resolution=$2
	dpi=$3

    out_filename=`echo ${in_filename} | sed 's/\.svg/\.png/g'`

    in_file="${src_dir}${divider}${in_filename}"
    out_file="${dst_dir}${divider}drawable-${resolution}${divider}${out_filename}"

    # It's only necessary to re-create the .png files if they are missing or the .svg has been updated
    if [ "${out_file}" -ot "${in_file}" ]; then
        inkscape_params="--without-gui \
            --file=\"${in_file}\" \
            --export-png=\"${out_file}\" \
            --export-dpi=\"${dpi}\" \
            --export-area-drawing"
	    inkscape_cmd="${inkscape_bin} ${inkscape_params} >/dev/null"
        eval ${inkscape_cmd}
        echo "${in_filename} ${resolution} updated";
    else
        echo "${in_filename} ${resolution} is already up-to-date"
    fi
}

# Set paths based on the system
case "$system" in
        "Linux")
		divider="/"
		src_dir="${1}"
        dst_dir="${2}"
		inkscape_bin=${inkscape_bin_linux}
        ;;
esac

echo "Source folder: ${src_dir}"
echo "Destination folder: ${dst_dir}"

for in_filename in `ls ${src_dir}`;
do
	convert ${in_filename} "ldpi"    "120" & \
	convert ${in_filename} "mdpi"    "160" & \
	convert ${in_filename} "hdpi"    "240"
	convert ${in_filename} "xhdpi"   "320" & \
	convert ${in_filename} "xxhdpi"  "480" & \
	convert ${in_filename} "xxxhdpi" "640"
done

exit 0
