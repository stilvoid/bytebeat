#!/bin/bash

set -e

function usage() {
    self=$(basename "$0")

    cat <<EOF >&2
Usage: $self EXPRESSION [-l LENGTH] [-o FILE] [-h]
Play the bytebeat EXPRESSION for LENGTH seconds.
LENGTH defaults to 16.
If FILE is supplied, $self outputs a wav file.
EOF

    if [ -n "$1" ]; then
        cat <<EOF >&2

Error: $1
EOF
    fi

    exit 1
}

# The expression
exp="$1"
shift

if [ -z "$exp" ]; then
    usage "EXPRESSION must be supplied"
fi

# Default length; 16 seconds
len=16

# blank means pipe through aplay
file=""

optstring=":l:o:h"

while getopts ${optstring} arg; do
    case "${arg}" in
        h)
            usage
            ;;
        o)
            file="${OPTARG}"
            ;;
        l)
            len="${OPTARG}"
            ;;
        :)
            usage "Must supply an argument to -$OPTARG"
            ;;
        ?)
            usage "Invalid option: -$OPTARG"
            ;;
    esac
done

# Ensure len is a number
if ! [[ "$len" =~ ^[0-9]+$ ]]; then
    usage "LENGTH must be a number"
fi

dir=$(mktemp -d)
trap 'rm -r "$dir"' SIGINT SIGTERM

cat <<EOF >"$dir/main.c"
#include <stdio.h>

int main(int t, char* a[]){
    for(;t<$((len*8000));t++) {
        putchar($exp);
    }
}
EOF

clang -w "$dir/main.c" -o "$dir/main"

if [ -z "$file" ]; then
    echo "Playing '$exp' for $len seconds"
    "$dir/main" | aplay 
else
    echo "Writing '$exp' for $len seconds to $file"
    "$dir/main" | sox -e unsigned-integer -b 8 -r 8000 -t raw - "$file"
fi
