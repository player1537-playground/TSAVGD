#!/bin/bash

function finish() {
    rm org
}

trap finish exit

root=..
src=$root/src
lib=$root/lib
ln -s $lib org
org=org
lwjgl=$org/lwjgl-2.8.4/jar
slick=$org/slick
slick_util=$org/slick-util

jar cmfv0 manifest windows.jar \
    -C $src character \
    -C $src splash \
    -C $src event \
    -C $src sound \
    -C $root res
#    $lwjgl/lwjgl.jar \
#    $lwjgl/lwjgl_util.jar \
#    $lwjgl/jinput.jar \
#    $slick/slick.jar \
#    $slick_util/slick-util.jar \
