#!/bin/bash
# Ugly tool to trigger automatic conversion from *.svg images to all sizes of *.png images
# TODO: to be tested on Linux
#

#
# Environment settings
#
src_dir="d:\\Workspace\\bhs_fueller\\app\\src\\main\\assets\\svg"
src_dir_moba="/drives/d/Workspace/bhs_fueller/app/src/main/assets/svg"
dst_root="d:\\Workspace\\bhs_fueller\\app\\src\\main\\res"
inkscape_bin='/drives/c/Program\ Files\ \(x86\)/Inkscape/inkscape.exe'

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

	dst_dir="${dst_root}\\drawable-${resolution}"
        out_file=`echo ${in_file} | sed 's/\.svg/\.png/g'`
	inkscape_params="--without-gui \
	                 --file=\"${src_dir}\\${in_file}\" \
			 --export-png=\"${dst_dir}\\${out_file}\" \
			 --export-dpi=\"${dpi}\" \
			 --export-area-drawing"
	inkscape_cmd="${inkscape_bin} ${inkscape_params} >/dev/null"
        eval ${inkscape_cmd}
}

for in_file in `ls ${src_dir_moba}`;
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
