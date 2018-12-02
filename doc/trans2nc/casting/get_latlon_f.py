# -*- coding: utf-8 -*-
"""
Created on Thu Oct 18 15:42:03 2018

@author: siwo
"""
import pyproj
import numpy as np
def get_latlon(factor):
    #各区域对应图例坐标、左上角、右下角经纬度、投影方式、图片分辨率
    region_all={'AECN':(523,537,597,793,113.506,38.8444,124.267,23.1044,'Lambert',600,800), #华东
                'ACCN':(722,333,798,589,103.626,35.3322,120.005,24.0566,'Lambert',800,600), #华中
                'ACHN':(948,613,1021,869,65.6571,46.4849,126.862,13.061,'Lambert',1024,880),#全国
                'ANCN':(725,5  ,799,261,104.643,43.0878,122.636,31.1917,'Lambert',800,600), #华北
                'ANEC':(723,564,797,820,114.539,54.224,131.91,36.3301,'Lambert',800,832), #东北
                'ABCJ':(884,98 ,955,348,96.4697,34.7539,126.494,25.0003,'Mercator',960,360),#长江流域
                'ABHH':(692,250,757,499,107.015,40.9176,123.65,30.4626,'Lambert',768,512),#黄淮流域
                'ACES':(721,537,789,789,103.491,35.7671,128.508,13.2104,'Mercator',800,800),#东南沿海
                'ASCN':(725,338,791,589,104.654,27.6771,121.318,16.104,'Mercator',800,600), #华南
                'ANWC':(749,6  ,1017,255,65.4922,43.212,109.31,29.4932,'Lambert',1024,768),#西北
                'ASWC':(727,10 ,791,218,96.1597,34.8143,112.812,20.7235,'Mercator',800,768)#西南
                }
    region=region_all[factor]
    if region[8]=='Mercator':
        p1 = pyproj.Proj(proj = 'merc', datum = 'WGS84')
    else:
        p1 = pyproj.Proj("+proj=lcc +lat_1=30 +lat_2=60 +lat_0=30 +lon_0=110  +no_defs +a=6378137  +rf=298.257223563 +to_meter=1")
    
    p2 = pyproj.Proj(proj = 'latlon', datum = 'WGS84')
    
    LUlon, LUlat = (region[4],region[5])
    RDlon, RDlat= (region[6],region[7])
    LUx, LUy = pyproj.transform(p2,p1,LUlon, LUlat)
    RDx, RDy = pyproj.transform(p2,p1,RDlon, RDlat)
    
    lenx,leny=(region[9],region[10])
    xar=np.linspace(LUx,RDx,lenx)
    yar=np.linspace(RDy,LUy,leny)
    
    yy=np.repeat(yar,lenx).reshape(leny,lenx)
    xx=np.transpose(np.repeat(xar,leny).reshape(lenx,leny))
    
    lon,lat=pyproj.transform(p1,p2,xx, yy)
    return lon,lat
