#!/bin/bash
# Ugly tool to trigger automatic conversion from *.svg images to all sizes of *.png images
# TODO: to be tested on Linux
#

#
# Environment settings
#
src_dir_win="d:\\Workspace\\bhs_fueller\\app\\src\\main\\assets\\svg"
src_dir_moba="/drives/d/Workspace/bhs_fueller/app/src/main/assets/svg"
src_dir_linux="/mnt/public/workspace/bhs_fueller/app/src/main/assets/svg"
dst_dir_win="d:\\Workspace\\bhs_fueller\\app\\src\\main\\res"
dst_dir_linux="/mnt/public/workspace/bhs_fueller/app/src/main/res"
inkscape_bin_win='/drives/c/Program\ Files\ \(x86\)/Inkscape/inkscape.exe'
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
	in_file=$1
	resolution=$2
	dpi=$3

	echo "  for size ${resolution}"

        out_file=`echo ${in_file} | sed 's/\.svg/\.png/g'`
	inkscape_params="--without-gui \
	                 --file=\"${src_dir}${divider}${in_file}\" \
			 --export-png=\"${dst_dir}${divider}drawable-${resolution}${divider}${out_file}\" \
			 --export-dpi=\"${dpi}\" \
			 --export-area-drawing"
	inkscape_cmd="${inkscape_bin} ${inkscape_params} >/dev/null"
        eval ${inkscape_cmd}
}

# Set paths based on the system
case "$system" in
        "Linux")
		echo "here"
		divider="/"
		svg_dir="${src_dir_linux}"
		src_dir="${src_dir_linux}"
                dst_dir="${dst_dir_linux}"
		inkscape_bin=${inkscape_bin_linux}
                ;;
        "CYGWIN")
		echo "or here"
		divider="\\"
		svg_dir="${src_dir_moba}"
		src_dir="${src_dir_win}"
		dst_dir="${dst_dir_win}"
		inkscape_bin=${inkscape_bin_win}
                ;;
esac

for in_file in `ls ${svg_dir}`;
do
	echo "converting ${in_file}..."
	convert ${in_file} "ldpi"    "120" & \
	convert ${in_file} "mdpi"    "160" & \
	convert ${in_file} "hdpi"    "240"
	convert ${in_file} "xhdpi"   "320" & \
	convert ${in_file} "xxhdpi"  "480" & \
	convert ${in_file} "xxxhdpi" "640"
done

exit 0
