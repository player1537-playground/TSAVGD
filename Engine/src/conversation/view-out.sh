#!/bin/bash

sed Out.java -e 's/[{};]/&\n/g' -i
if [ -z "$1" ]; then
    vim +'set cinoptions+=j1' +'normal ggvG=' +wq Out.java
else
    emacs -nw Out.java
fi
cat Out.java 
