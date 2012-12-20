#!/bin/bash

if [ -z "$1" ]; then
    echo "Usage: $(basename $0) package.ClassName"
    echo "  Runs package.ClassName (if you are located in Engine/)"
    exit 1
fi

if ! [ -d lib -a -d src -a -d res ]; then
    echo "You're in the wrong directory, move to Engine/"
    exit 2
fi

LIB=lib
java -cp src/:$LIB/lwjgl-2.8.4/jar/lwjgl.jar:$LIB/lwjgl-2.8.4/jar/lwjgl_util.jar:$LIB/lwjgl-2.8.4/jar/jinput.jar:$LIB/slick-util/slick-util.jar:$LIB/slick/slick.jar: -Djava.library.path=$LIB/lwjgl-2.8.4/native/linux/ event.EventTest