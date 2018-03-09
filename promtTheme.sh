PS1="\n\[\033[1;33m\]files($(/bin/ls -1 | /usr/bin/wc -l | /bin/sed 's: ::g')) \[\033[1;33m\]\$(/bin/ls -lah | /bin/grep -m 1 total | /bin/sed 's/total //') \[\033[38;5;95m\]\[$(tput bold)\]\$(/bin/pwd)\n\[$(tput setaf 2)\]-> \[\033[0m\]"

PS1="\n\[\033[1;33m\]files($(/bin/ls -1 | /usr/bin/wc -l | /bin/sed 's: ::g')) \[\033[1;33m\]\$(/bin/ls -lah | /bin/grep -m 1 total | /bin/sed 's/total //') \[\033[38;5;95m\]\[$(tput bold)\]\$(/bin/pwd)\n\[$(tput setaf 2)\]-> \[\033[0m\]\[$(tput bold)"

// mac
PS1="\n\[\033[1;33m\]files($(ls -1 | wc -l | sed 's: ::g')) \$(parse_git_branch)\[\033[00m\] \[$(tput bold)\]\$(/bin/pwd)\n\[$(tput setaf 2)\]-> \[\033[0m\]"

// el completo
PS1="\n\033[0;36m\]time[\T]\[\033[00m\] \033[0;35m\]history[\!]\[\033[00m\] \033[0;31m\]ram[$(free_mem)]\[\033[00m\] \033[38;5;198m\][\$(command_ok)]\[\033[00m\]\n\033[0;33m\]\u@\h\[\033[00m\] \033[1;33m\]$(/bin/pwd)\[\033[00m\]\033[97m\]$(parse_git_branch)\[\033[00m\]\n$\[\]-> \[\033[0m\]"

// functions
parse_git_branch() {
     git branch 2> /dev/null | sed -e '/^[^*]/d' -e 's/* \(.*\)/ (\1)/'
}

free_mem(){
    awk '/MemFree/{print $2}' /proc/meminfo
}

command_ok(){
    if [ $? = 0 ]; then
        echo "✔";
    else
        echo "✘";
    fi
}

