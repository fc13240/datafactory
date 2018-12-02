# -*- coding: utf-8 -*-
"""
Created on Thu Oct 11 15:27:44 2018

@author: siwo
"""

# -*- coding: utf-8 -*-
"""
Created on Mon Oct  1 09:29:49 2018

@author: siwo
"""
#import colorsys
import struct
from PIL import Image
import numpy as np
import sys,os

def pic2data(path,name,outpath):
#    path='D:\\moonkin\\雷达图识别\\LeiDa\\ChangGui\\'
#    name='Z_RADA_C_BABJ_20180915151200_P_DOR_RDCP_R_ANCN.PNG'
    
    factor=name.split('_')[-2]
    region=name.split('_')[-1][0:4]
    datatime=name.split('_')[4]
    
#    outpath='D:\\moonkin\\雷达图识别\\data_out\\'
    #outname='MOP_'+region+'_'+'factor'+'_'+datatime+'.latlon'
    #outname=name.replace('.PNG','.latlon')
    outname=datatime+'.latlon'
    #截取一部分图片
    #img_src = img_src.crop((0,70,680,511))
    
    # In[]
    #各区域对应图例坐标
    region_all={'AECN':(523,537,597,793),
                'ACCN':(722,333,798,589),
                'ACHN':(948,613,1021,869),
                'ANCN':(725,5  ,799,261),
                'ANEC':(723,564,797,820),
                'ABCJ':(884,98 ,955,348),
                'ABHH':(692,250,757,499),
                'ACES':(721,537,789,789),
                'ASCN':(725,338,791,589),
                'ANWC':(749,6  ,1017,255),
                'ASWC':(727,10 ,791,218)
                }
    #'R','CR'的颜色
    if factor in ['R','CR']:
        '''R & CR'''
        colors={(1, 160, 246, 255):12.5,
                (0, 216, 0, 255):22.5,
                (255, 0, 240, 255):62.5,
                (231, 192, 0, 255):37.5,
                (214, 0, 0, 255):52.5,
                (192, 0, 0, 255):57.5,
                (173, 144, 240, 255):72.5,
                (150, 0, 180, 255):67.5,
                (0, 236, 236, 255):17.5,
                (255, 144, 0, 255):42.5,
                (255, 0, 0, 255):47.5,
                (1, 144, 0, 255):27.5,
                (255, 255, 0, 255):32.5}
    #'''VIL的颜色'''
    elif factor == 'VIL':
        colors={(1, 255, 0, 255):12.5,
                (0, 200, 0, 255):17.5,
                (255, 0, 240, 255):57.5,
                (231, 192, 0, 255):32.5,
                (214, 0, 0, 255):47.5,
                (192, 0, 0, 255):52.5,
                (173, 144, 240, 255):72.5,
                (0, 236, 236, 255):8.5,
                (120, 0, 132, 255):67.5,
                (255, 144, 0, 255):37.5,
                (255, 0, 0, 255):42.5,
                (216, 0, 156, 255):62.5,
                (255, 255, 0, 255):27.5,
                (1, 144, 0, 255):22.5}
    #'''OHP的颜色'''
    elif factor == 'OHP':
        colors={(255, 0, 240, 255):112.5, #100-125
                (214, 0, 0, 255):69, #63-75
                (192, 0, 0, 255):87.5,#75-100
                (173, 144, 240, 255):130, #130-
                (0, 236, 236, 255):17.5, #10-25
                (255, 144, 0, 255):44, #38-50
                (255, 0, 0, 255):56.5, #50-63
                (0, 0, 246, 255):2.5, #0-5
                (255, 255, 0, 255):31.5, #25-38
                (1, 160, 246, 255):7.5} #5-10
    '''河流、行政边界、文字的颜色'''
    word_color=[(104, 104, 104, 255),
                #(253, 255, 254, 255),
                (204, 204, 204, 255),
                (190, 232, 255, 255),
                (156, 156, 156, 255),
                (130, 130, 130, 255)]
    # In[]
    point1=[811,116,31.697793,121.890564]  #崇明的横坐标、纵坐标纬度、经度
    point2=[170,322,26.046913,101.821289]  #攀枝花横坐标、纵坐标纬度、经度
    
    dlat=(point1[2]-point2[2])/(point1[1]-point2[1]) #单个像素点的纬距
    dlon=(point1[3]-point2[3])/(point1[0]-point2[0]) #单个像素点的经距
    
    # In[]
    img_src = Image.open(path+name)
    img_size=img_src.size
    #画原图
    #plt.figure(figsize=(img_size[0]/40,img_size[1]/40))
    #im=plt.imshow(img_src)
    #定义和图片大小一致的0矩阵
    image_value=np.zeros((img_size[0],img_size[1]))
    
    src_strlist = img_src.load()
    
    #左上角的经纬度
    x0_lon=point1[3]+(0-point1[0])*dlon
    y0_lat=point1[2]+(0-point1[1])*dlat
    
    #右下角的经纬度
    x1_lon=point1[3]+(img_size[0]-point1[0])*dlon
    y1_lat=point1[2]+(img_size[1]-point1[1])*dlat
    
    
    
    '''获取格点数据，没有数据为0，河流、行政区域、文字数据为1'''
    for i in range(0,img_size[0],1):
        for j in range(0,img_size[1],1):
    #        print(src_strlist[i,j])
            if src_strlist[i,j] in colors: #是否有数据
                image_value[i,j]=colors[src_strlist[i,j]]
            elif src_strlist[i,j] in word_color: #是否是河流等
                image_value[i,j]=1
            else: #是不需要的点
                image_value[i,j]=0
    '''找到图例并填充为0'''
    region_del=region_all[region]
    image_value[region_del[0]:region_del[2],region_del[1]:region_del[3]]=0
    # In[]
    '''对河流、文字、行政边界的点进行插值'''
    image_out=np.copy(image_value)
    for i in range(0,img_size[0],1):
        for j in range(0,img_size[1],1):
            #判断是否为河流等点
            if image_value[i,j]==1:
                sum_value=0  #周围所有有效点的值的和
                count_1=0    #附近河流等点的个数
                count_0=0    #附近无效点的个数
                count_c=0    #附近数据点的个数
                #取该点周围8各点进行判断
                for n in [(i-1,j-1),(i-1,j  ),(i-1,j+1),
                          (i  ,j-1),          (i  ,j+1),
                          (i+1,j-1),(i+1,j  ),(i+1,j+1)]:
                    if image_value[n] > 1:
                        count_c += 1
                        sum_value += image_value[n]
                    elif image_value[n] == 1:
                        count_1 += 1
                    else:
                        count_0 += 1
                #周围没有数据点，且有无效点
                if count_c == 0 and count_0 > 0:
                    image_out[i,j]=0
                #有数据点
                elif count_c >0:
                    try:
                        #数据点占多数
                        if count_c / count_0 > 0.5:
                            image_out[i,j]=sum_value/count_c
                        #数据点占少数
                        elif count_c / count_0 <= 0.5:
                            image_out[i,j]=0
                    except ZeroDivisionError: #无效点个数可能为0
                        image_out[i,j]=sum_value/count_c
                #数据点和无效点都没有
                elif count_1 == 8:
                    #判断再往外的一层
                    for n in [(i-2,j-2),(i-2,j-1),(i-2,j  ),(i-2,j+1),(i-2,j+2),
                              (i-1,j-2)                              ,(i-1,j+2),
                              (i  ,j-2)                              ,(i  ,j+2),
                              (i+1,j-2)                              ,(i+1,j+2),
                              (i+2,j-2),(i+2,j-1),(i+2,j  ),(i+2,j+1),(i+2,j+2)]:
                        if image_value[n] > 1:
                            count_c += 1
                            sum_value += image_value[n]
                        elif image_value[n] == 1:
                            count_1 += 1
                        else:
                            count_0 += 1
                    #没有数据点
                    if count_c == 0:
                        image_out[i,j]=0
                    #有数据点
                    elif count_c >0:
                        try:
                            #数据点占多数
                            if count_c / count_0 > 0.5:
                                image_out[i,j]=sum_value/count_c
                            #数据点占少数
                            elif count_c / count_0 <= 0.5:
                                image_out[i,j]=0
                        except ZeroDivisionError: #无效点个数可能为0
                            image_out[i,j]=sum_value/count_c
    # In[]
    '''生成数据'''
    #转置
    image_out=np.transpose(image_out)
    #倒y
    image_out=image_out[::-1]
    #转置
    image_out=np.transpose(image_out)
    out=[]
    for y in range(0,img_size[1],1): #y
        ifFirst=True
        for x in range(0,img_size[0],1):
            if image_out[x,y] > 0:
                if ifFirst:
                    if x!=img_size[0]-1:
                        ifFirst=False
                        out_tmp=[]
                        count=1
                        out_tmp.extend([image_out[x,y]])
                    else:
                        out.extend([y,x,1])
                        out.extend([image_out[x,y]])
                else:
                    if x!=img_size[0]-1:
                        out_tmp.extend([image_out[x,y]])
                        count+=1
                    else:
                        out_tmp.extend([image_out[x,y]])
                        count+=1
                        out.extend([y,x-count+1,count])
                        out.extend(out_tmp)
            elif image_out[x,y]==0 and not ifFirst:
                out.extend([y,x-count,count])
                out.extend(out_tmp)
                ifFirst=True            
    out.extend([-1,-1,-1])
    for i in out:
        try:
            data_b+=struct.pack('h',int(i))
        except NameError:
            data_b=struct.pack('h',int(i))
    
    # In[]
    '''生成文件头'''
    
    dataname= outname.encode('utf-8')+b'\x00'*(128-len(outname))
    varname =factor.encode('utf-8')+ b'\x00'*(32-len(factor))
    char    ='dBZ'.encode('utf-8')+ b'\x00'*(16-len('dBZ'))
    label   = 0 #unsigned short
    unitlen = 2 #short
    slat=y0_lat*1000 #float
    wlon=x0_lon*1000#float
    nlat=y1_lat*1000#float
    elon=x1_lon*1000#float
    clat=(y1_lat+y0_lat)/2*1000#float
    clon=(x1_lon+x0_lon)/2*1000#float
    rows=img_size[1] #	int
    cols=img_size[0] #	int
    #dlat float
    #dlon float
    nodata=0.0 #float
    levelbytes =0  #long
    levelnum=0 #short
    amp= 0 #short
    compmode =1 #short 
    dates = 0 #unsigned short
    seconds= 0 #int
    min_value =  0 #short
    max_value =  0 #short
    #reserved = 
    
    head =dataname + varname + char +struct.pack('Hh6f2i3f2l3hHi2h6h', label,unitlen,slat,wlon,nlat,
                                                 elon,clat,clon,rows,cols,abs(dlat*1000),abs(dlon*1000),nodata,
                                                 levelbytes,levelbytes,levelnum,amp,compmode,
                                                 dates,seconds,min_value,max_value,
                                                 0,0,0,0,0,0);
    
    
    # In[]
    '''存储'''
    all_out=head+data_b
    with open(outpath+outname, 'wb') as f:
        f.write(all_out)
        
# In[]
if __name__=="__main__":
    env = os.path.realpath(__file__) + '/venv/Scripts/'
    os.system(env + "activate")
    pic2data(sys.argv[1],sys.argv[2],sys.argv[3])