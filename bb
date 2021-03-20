#!/bin/bash

#set -e

function usage() {
    self=$(basename "$0")

    cat <<EOF >&2
Usage: $self [OPTION...] EXPRESSION

Plays the bytebeat EXPRESSION as audio.

Options:
  -h,--help           Display this message
  -l,--length LENGTH  Play audio for LENGTH seconds (default 16)
  -o,--output FILE    Output autio to FILE as 8-bit WAV
EOF

    if [ -n "$1" ]; then
        cat <<EOF >&2

Error: $1
EOF
    fi

    exit 1
}

SOX="sox -q -e unsigned-integer -b 8 -r 8000 -t raw -"

# Default length; 16 seconds
len=16

# blank means pipe through aplay
file=""

# Empty expression
exp=""

while :; do
    case $1 in
        -h|--help)
            usage
            ;;
        -o|--output)
            if [ "$2" ]; then
                file="$2"
                shift
            else
                usage "$1 requires a non-empty option argument"
            fi
            ;;
        -l|--length)
            if [ "$2" ]; then
                len="$2"

                if ! [[ "$len" =~ ^[0-9]+$ ]]; then
                    usage "$1 requires an integer argument"
                fi

                shift
            else
                usage "$1 requires a non-empty option argument"
            fi
            ;;
        *)
            if [ -z "$exp" ]; then
                exp="$1"
            else
                usage "Unexpected extra argument: $1"
            fi
            ;;
    esac

    shift

    if [ $# == 0 ]; then
        break
    fi
done

# Check the expression
if [ -z "$exp" ]; then
    usage "EXPRESSION must not be empty"
fi

# Create a temporary folder - clean it up when we exit
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
    "$dir/main" | $SOX -d
else
    "$dir/main" | $SOX "$file"
fi
