#Utiliy to copy a file and replace some of the contents
#This is useful when creating implementations for versions which are similar to other,
#for example: versions v1_16_R3 and v1_16_R2 don't have many differences so the only
#thing needed is to rename the class names and import paths

import sys

def replace(input, search, replace):
    with open(input, "rt") as fin:
        with open(input.replace(search, replace), "wt") as fout:
            for line in fin:
                fout.write(line.replace(search, replace))


for i in range(len(sys.argv)-3):
    replace(sys.argv[i+3], sys.argv[1], sys.argv[2])