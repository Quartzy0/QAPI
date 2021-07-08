#Utiliy to copy a file and replace some of the contents
#This is useful when creating implementations for versions which are similar to other,
#for example: versions v1_16_R3 and v1_16_R2 don't have many differences so the only
#thing needed is to rename the class names and import paths

import sys

with open(sys.argv[1], "rt") as fin:
    with open(sys.argv[1].replace(sys.argv[2], sys.argv[3]), "wt") as fout:
        for line in fin:
            fout.write(line.replace(sys.argv[2], sys.argv[3]))

