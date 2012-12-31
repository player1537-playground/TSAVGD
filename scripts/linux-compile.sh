#!/bin/bash

LIB=../../lib
first=${1:-.}
shift
files=
for i in $first "$@"; do
    for f in ${i%%/}/*.java; do
	files="${files} "$f
    done
done

javac -cp .:$LIB/lwjgl-2.8.4/res:$LIB/lwjgl-2.8.4/jar/lwjgl.jar:$LIB/lwjgl-2.8.4/jar/lwjgl_test.jar:$LIB/lwjgl-2.8.4/jar/lwjgl_util.jar:$LIB/lwjgl-2.8.4/jar/jinput.jar:$LIB/slick-util/slick-util.jar:$LIB/slick/slick.jar: $files