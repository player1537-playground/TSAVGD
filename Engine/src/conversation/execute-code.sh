#!/bin/bash

x="$(cat testing.scm | tr -d "\n\t")"
python proof-of-concept.py "$x" && ./view-out.sh 
