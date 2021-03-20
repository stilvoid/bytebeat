#!/bin/bash

set -e

dir=$(mktemp -d)
trap 'rm -r "$dir"' SIGINT SIGTERM

cat <<EOF >"$dir/main.c"
#include <stdio.h>

int main(int t, char* a[]){
    for(;;t++) {
        putchar($1);
    }
}
EOF

clang -w "$dir/main.c" -o "$dir/main"

"$dir/main" | aplay 
