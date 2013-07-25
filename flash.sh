#!/bin/bash
echo "\$#="$#
if [[ $#>0 ]]; then
	#statements
	flash="$1"
	if [[ -f "$flash" ]]; then
		#statements
		if [ "${flash##*.}" = "sh" ]; then
			echo "3"
			chmod a+x "$flash"
			adb reboot bootloader
			./"$flash"			
		else
			echo "4"
		fi

	else
		echo "2"		
	fi
else
	echo "1"
fi