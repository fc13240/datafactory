# -*- coding: utf-8 -*-
"""
Created on Tue Sep 25 11:14:11 2018

@author: siwo
"""
import struct
import numpy as np
from netCDF4 import Dataset
import time
import sys,os

def change_factor(factor_in):
    dictnory={'height':'HGT',
              'height-p':'HGT-p',
              'temper':'TMP',
              'temper-p':'TMP-p',
              'rh':'RH',
              'rh-p':'RH-p',
              #'wind':'WIND',
              #'wind-p':'WIND-p',
              'div':'DIV',
              'div-p':'DIV-p',
              'vor':'PV',
              'vor-p':'PV-p',
              'omega':'W',
              'omega-p':'W-p',
              'slp':'PRMSL',
              'slp-p':'PRMSL-p',
              't2':'TMP2M',
              't2-p':'TMP2M-p',
              'td2':'TD2M',
              'td2-p':'TD2M-p',
              'nc':'TCC',
              'nc-p':'TCC-p',
              'hc':'HCC',
              'hc-p':'HCC-p',
              'mc':'MCC',
              'mc-p':'MCC-p',
              'lc':'LCC',
              'lc-p':'LCC-p',
              'dp24':'DP24',
              'dp24-p':'DP24-p',
              'dt24-p':'DT24-p',
              'dh24':'DH24',
              'dh24-p':'DH24-p',
              'dt24':'DT24',
              'rain3':'P3',
              'rain3-p':'P3-p',
              'rain6':'P6',
              'rain6-p':'P6-p',
              'rain12':'P12',
              'rain12-p':'P12-p',
              'rain24':'P24',
              'rain24-p':'P24-p'
              }
    return dictnory[factor_in]
def write_to_nc_wanmei(data,file_name_path,factor,level,axis):
    try:
        da=Dataset(file_name_path,'a',format='NETCDF4')
        try:
            if level<999:
                levS=np.array([10,20,30,50,70,100,200,300,500,700,850,925])
                index=np.argwhere(levS == level)
                da.variables[factor][index[0,0]]=data
            else:
                da.variables[factor][:]=data       #填充数据 
        except KeyError:
            if level<999:
                da.createVariable(factor,'f',('lev','lat','lon'))
                levS=np.array([10,20,30,50,70,100,200,300,500,700,850,925])
                index=np.argwhere(levS == level)
                data_defort=np.ones((12,axis[5],axis[4]))*9999
                da.variables[factor][:]=data_defort
                da.variables[factor][index[0,0]]=data
            else:
                da.createVariable(factor,'f',('lat','lon'))
                da.variables[factor][:]=data
        da.close()
    except FileNotFoundError:
        da=Dataset(file_name_path,'w',format='NETCDF4')
        lonS=np.linspace(axis[0],axis[1],axis[4])
        latS=np.linspace(axis[2],axis[3],axis[5])
        levS=np.array([10,20,30,50,70,100,200,300,500,700,850,925])
        da.createDimension('lon',axis[4])  #创建坐标点
        da.createDimension('lat',axis[5])  #创建坐标点
     
        
        da.createDimension('lev',12)   #创建坐标点        
        longitudes=da.createVariable("lon",'f',("lon"))  #添加coordinates  'f'为数据类型，不可或缺
        latitudes=da.createVariable("lat",'f',("lat"))  #添加coordinates  'f'为数据类型，不可或缺
        da.createVariable("lev",'i',("lev")) #创建变量，shape=(181,361)  'f'为数据类型，不可或缺
        da.variables['lat'][:]=latS     #填充数据
        da.variables['lon'][:]=lonS     #填充数据
        da.variables['lev'][:]=levS     #填充数据
        
        longitudes.units="degrees_east"
        longitudes.Lo1=axis[0]
        longitudes.Lo2=axis[1]
        longitudes.Dx =(axis[1]-axis[0])/(axis[4]-1)
        longitudes.La1=axis[2]
        longitudes.La2=axis[3]
        longitudes.Dy =(axis[3]-axis[2])/(axis[5]-1)
        latitudes.units="degrees_north"
        latitudes.La1=axis[2]
        latitudes.La2=axis[3]
        latitudes.Dy =(axis[3]-axis[2])/(axis[5]-1)
        latitudes.Lo1=axis[0]
        latitudes.Lo2=axis[1]
        latitudes.Dx =(axis[1]-axis[0])/(axis[4]-1)        
#        da.createVariable('TMP','f',('lat','lon')) 
#        da.createVariable('RH','f',('lat','lon'))   
#        da.createVariable('WIND','f',('lat','lon'))
#        da.createVariable('DIV','f',('lat','lon'))
#        da.createVariable('PV','f',('lat','lon'))
#        da.createVariable('W','f',('lat','lon'))
#        da.createVariable('PRMSL','f',('lat','lon'))
#        da.createVariable('TMP2M','f',('lat','lon'))
#        da.createVariable('TD2M','f',('lat','lon'))
#        da.createVariable('TCC','f',('lat','lon'))
#        da.createVariable('HCC','f',('lat','lon'))
#        da.createVariable('MCC','f',('lat','lon'))
#        da.createVariable('LCC','f',('lat','lon'))
#        da.createVariable('DP24','f',('lat','lon'))
#        da.createVariable('DH24','f',('lat','lon'))
#        da.createVariable('DT24','f',('lat','lon'))
#        da.createVariable('P3','f',('lat','lon'))
#        da.createVariable('P6','f',('lat','lon'))
#        da.createVariable('P12','f',('lat','lon'))
#        da.createVariable('P24','f',('lat','lon'))
        if level<999:
            da.createVariable(factor,'f',('lev','lat','lon'))
            levS=np.array([10,20,30,50,70,100,200,300,500,700,850,925])
            index=np.argwhere(levS == level)
            data_defort=np.ones((12,axis[5],axis[4]))*9999
            da.variables[factor][:]=data_defort
            da.variables[factor][index[0,0]]=data
        else:
            da.createVariable(factor,'f',('lat','lon'))
            da.variables[factor][:]=data        
        da.close()

def file_trans(filepath,filename,outpath):
    if filename[0:6]=='zc_mwf':
        start=time.time()
        name_list=filename.split('_')
        factor=name_list[2]
        level =name_list[3]
        report_time=name_list[4][0:12]
        forecast_time=name_list[4][13:16]
        binFile=open(filepath+filename,'rb')
        
        
        binFile.seek(5)
        context1=binFile.read(1*3)
        data_raw1=struct.unpack('s'*3,context1)
        tmp=''
        for i in data_raw1:
            tmp=tmp+str(i,encoding = "utf-8")
        if tmp=='104':
            '''起始经纬度信息'''
            binFile.seek(280)
            context=binFile.read(1*42)
            data_raw=struct.unpack('s'*42,context)
            tmp1=''
            for i in data_raw:
                tmp1=tmp1+str(i,encoding = "utf-8")
            axis=tmp1.split(' ')
            while '' in axis:
                axis.remove('')
            lon0=float(axis[2])
            lon1=float(axis[3])
            lat0=float(axis[4])
            lat1=float(axis[5])
            lon_p=int(axis[0])
            lat_p=int(axis[1])    
        
        elif tmp=='106':
            '''起始经纬度信息'''
            binFile.seek(214)
            context=binFile.read(1*24)
            data_raw=struct.unpack('s'*24,context)
            tmp1=''
            for i in data_raw:
                tmp1=tmp1+str(i,encoding = "utf-8")
            axis=tmp1.split(' ')
            while '' in axis:
                axis.remove('')
            lon0=float(axis[0])
            lon1=float(axis[1])
            lat0=float(axis[2])
            lat1=float(axis[3])
            
            '''经纬度格点数'''
            binFile.seek(280)
            context=binFile.read(1*10)
            data_raw=struct.unpack('s'*10,context)
            tmp2=''
            for i in data_raw:
                tmp2=tmp2+str(i,encoding = "utf-8")
            p_axis=tmp2.split(' ')
            while '' in p_axis:
                p_axis.remove('')
            lon_p=int(p_axis[0])
            lat_p=int(p_axis[1])
        elif tmp=='112' or tmp=='113':
            '''起始经纬度信息'''
            binFile.seek(216)
            context=binFile.read(1*42)
            data_raw=struct.unpack('s'*42,context)
            tmp1=''
            for i in data_raw:
                tmp1=tmp1+str(i,encoding = "utf-8")
            axis=tmp1.split(' ')
            while '' in axis:
                axis.remove('')
            lon0=float(axis[0])
            lon1=float(axis[1])
            lat0=float(axis[2])
            lat1=float(axis[3])
            lon_p=int(axis[4])
            lat_p=int(axis[5])
        
        
        #binFile.seek(280)
        #title=binFile.read(4)
        #title_raw=struct.unpack('i',title)
        binFile.seek(360)
        if factor[0:4]!='wind':
            context=binFile.read(4*lon_p*lat_p)
            data_raw=struct.unpack('f'*lon_p*lat_p,context)
            binFile.close()
            verify_data =  np.asarray(data_raw).reshape(lat_p,lon_p)
            fileout='_'.join([report_time,forecast_time+'.nc'])
            write_to_nc_wanmei(verify_data,outpath+fileout,change_factor(factor),int(level),[lon0,lon1,lat0,lat1,lon_p,lat_p])
        else:
            context=binFile.read(4*lon_p*lat_p*2)
            try:
                data_raw=struct.unpack('f'*lon_p*lat_p*2,context)
                binFile.close()
                verify_data =  np.asarray(data_raw).reshape(lat_p*2,lon_p)
                fileout='_'.join([report_time,forecast_time+'.nc'])
                write_to_nc_wanmei(verify_data[0:lat_p,:],outpath+fileout,factor+'-s',int(level),[lon0,lon1,lat0,lat1,lon_p,lat_p])
                write_to_nc_wanmei(verify_data[lat_p:,:],outpath+fileout,factor+'-d',int(level),[lon0,lon1,lat0,lat1,lon_p,lat_p])
            except:
                1
        end=time.time()
        print(filename + 'has been transformed to nc. Time costs:',end-start)
    else:
        print(filename + 'is not the file we need.')

if __name__=="__main__":
    env = os.path.realpath(__file__) + '/venv/Scripts/'
    os.system(env + "activate")
    file_trans(sys.argv[1],sys.argv[2],sys.argv[3])

