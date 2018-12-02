# -*- coding: utf-8 -*-
"""
Created on Mon Oct 22 19:42:20 2018

@author: siwo
"""
path='/home/nankong/pic2latlon/casting/'
lists=['R','OHP','VIL','CR']
for i in lists:
    name=path+i+'.log'
    with open(name,'w') as f:
        f.write('Z__0')