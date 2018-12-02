# -*- coding: utf-8 -*-
"""
Created on Mon May 28 19:11:14 2018

@author: moonkin
"""
import os

def get_log(log_file):
    filelist=[]
    if os.path.exists(log_file):
        fl=open(log_file, 'r')
        for lines in fl:
            filelist.append(lines.strip())
        fl.close()
        filelist.sort()
    return filelist

def write_log(log_file,filelist):
##    filelist.sort()
#    filelist2 = {}.fromkeys(filelist).keys()
#    filelist2.sort()
    with open(log_file, 'w') as fl:
        for filel in filelist:
            fl.write(filel)
            fl.write('\n')
