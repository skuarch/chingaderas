PS1="\n\[\033[1;33m\]files($(/bin/ls -1 | /usr/bin/wc -l | /bin/sed 's: ::g')) \[\033[1;33m\]\$(/bin/ls -lah | /bin/grep -m 1 total | /bin/sed 's/total //') \[\033[38;5;95m\]\[$(tput bold)\]\$(/bin/pwd)\n\[$(tput setaf 2)\]-> \[\033[0m\]"

PS1="\n\[\033[1;33m\]files($(/bin/ls -1 | /usr/bin/wc -l | /bin/sed 's: ::g')) \[\033[1;33m\]\$(/bin/ls -lah | /bin/grep -m 1 total | /bin/sed 's/total //') \[\033[38;5;95m\]\[$(tput bold)\]\$(/bin/pwd)\n\[$(tput setaf 2)\]-> \[\033[0m\]\[$(tput bold)"

// mac
PS1="\n\[\033[1;33m\]files($(ls -1 | wc -l | sed 's: ::g')) \$(parse_git_branch)\[\033[00m\] \[$(tput bold)\]\$(/bin/pwd)\n\[$(tput setaf 2)\]-> \[\033[0m\]"

// function
parse_git_branch() {
     git branch 2> /dev/null | sed -e '/^[^*]/d' -e 's/* \(.*\)/ (\1)/'
}
