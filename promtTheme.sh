PS1="\n\[\033[1;33m\]files($(/bin/ls -1 | /usr/bin/wc -l | /bin/sed 's: ::g')) \[\033[1;33m\]\$(/bin/ls -lah | /bin/grep -m 1 total | /bin/sed 's/total //') \[\033[38;5;95m\]\[$(tput bold)\]\$(/bin/pwd)\n\[$(tput setaf 2)\]-> \[\033[0m\]"

PS1="\n\[\033[1;33m\]files($(/bin/ls -1 | /usr/bin/wc -l | /bin/sed 's: ::g')) \[\033[1;33m\]\$(/bin/ls -lah | /bin/grep -m 1 total | /bin/sed 's/total //') \[\033[38;5;95m\]\[$(tput bold)\]\$(/bin/pwd)\n\[$(tput setaf 2)\]-> \[\033[0m\]\[$(tput bold)"

// mac
PS1='\n\[\033[1;31m\]$(date +%H:%M:%S) \[\033[1;33m\]$(pwd) \[\033[1;34m\]$(parse_git_branch) \n\[\033[1;35m\]-> \[\033[0m\] '

// el completo
PS1="\n\033[0;36m\]time[\T]\[\033[00m\] \033[0;35m\]history[\!]\[\033[00m\] \033[0;31m\]ram[\$(free_mem)]\[\033[00m\]\n\033[0;33m\]\u@\h\[\033[00m\] \033[1;33m\]\$(/bin/pwd)\[\033[00m\]\033[97m\]\$(parse_git_branch)\[\033[00m\]\n$\[\]-> \[\033[0m\]"

// sin fallas y completo
PS1="\n\033[0;36m\][\$(/bin/pwd)]\[\033[00m\] \033[0;35m\][\W]\[\033[00m\] \033[0;32m\][\$(command_ok)]\[\033[00m\] \033[0;31m\][\$(get_governor)]\[\033[00m\] \033[0;33m\][\$(get_batery_percentage)]\[\033[00m\] \033[0;37m\][\$]\[\033[00m\]\033[0;32m\]\$(parse_git_branch)\[\033[00m\] \n-> "

// functions
parse_git_branch() {
     git branch 2> /dev/null | sed -e '/^[^*]/d' -e 's/* \(.*\)/ (\1)/'
}

free_mem(){
    awk '/MemFree/{print $2}' /proc/meminfo
}

command_ok(){
    if [ $? != 0 ]; then
        echo "✘";        
    else
        echo "✔";
    fi
}

get_governor() {
    cat /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor
}

get_batery_percentage(){
    upower -i $(upower -e | grep 'BAT') | grep -E "state|to\ full|percentage" | awk '/perc/{print $2}'
}

