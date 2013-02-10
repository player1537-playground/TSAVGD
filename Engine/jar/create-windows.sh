#!/bin/bash

function finish() {
    rm org
}

trap finish exit

function getclasses() {
    local folder
    folder=$1
    for f in $folder/*.class; do
	echo -C $folder ${f##*\/}
    done
}

root=..
src=$root/src
lib=$root/lib
ln -s $lib org
org=org
lwjgl=$org/lwjgl-2.8.4/jar
slick=$org/slick
slick_util=$org/slick-util

jar cmfv0 manifest windows.jar \
    $(getclasses $src/character) \
    $(getclasses $src/splash) \
    $(getclasses $src/event) \
    $(getclasses $src/sound) \
    -C $root res
#    $lwjgl/lwjgl.jar \
#    $lwjgl/lwjgl_util.jar \
#    $lwjgl/jinput.jar \
#    $slick/slick.jar \
#    $slick_util/slick-util.jar \
