#! /bin/bash
set -x
# 数据集路径
mkdir -p /nfs/$DUBHE_ENVIRONMENT/dataset/
# 算法文件路径
mkdir -p /nfs/$DUBHE_ENVIRONMENT/algorithm-manage/
# 预置算法文件路径
mkdir -p /nfs/$DUBHE_ENVIRONMENT/algorithm-manage/common/
# 训练文件路径
mkdir -p /nfs/$DUBHE_ENVIRONMENT/train-manage/
# 模型文件路径
mkdir -p /nfs/$DUBHE_ENVIRONMENT/model/
# 预置模型文件路径
mkdir -p /nfs/$DUBHE_ENVIRONMENT/model/common/
mkdir -p /nfs/$DUBHE_ENVIRONMENT/exported-metrics/
# 模型优化文件路径
mkdir -p /nfs/$DUBHE_ENVIRONMENT/model-opt/
# 上传文件临时路径
mkdir -p /nfs/$DUBHE_ENVIRONMENT/upload-temp/
# 镜像文件路径
mkdir -p /nfs/$DUBHE_ENVIRONMENT/upload-image/
# 云端部署文件路径
mkdir -p /nfs/$DUBHE_ENVIRONMENT/serving/
mkdir -p /nfs/$DUBHE_ENVIRONMENT/serving/dubhe_serving

# Clone code in nfs
cd /nfs

rsync -urv /dubhe-code/* /nfs/

# Copy dubhe_data_process
rsync -urv /nfs/Dubhe/dubhe_data_process ./

# Copy serving
rsync -urv /nfs/Dubhe/tianshu_serving/* ./$DUBHE_ENVIRONMENT/serving/dubhe_serving 
