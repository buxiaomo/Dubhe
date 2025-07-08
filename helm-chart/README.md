# dubhe helm
```
cp 
helm upgrade -i dubhe ./ -n dubhe-system --create-namespace \
--set global.storageClassName=local-path \
--set dubhe.cicd.enabled=true 


helm uninstall dubhe -n dubhe-system
```

{# 
BACKEND_PASSWORD
BACKEND_SERVER

HARBOR_SERVER
HARBOR_USERNAME
HARBOR_PASSWORD
OPTIMIZE_IMAGE
TADL_IMAGE #}


kubectl  delete deployments.apps -n dubhe-system backend-visual backend-train backend-terminal backend-tadl backend-serving-gateway backend-serving backend-point-cloud backend-optimize backend-notebook backend-model backend-measure backend-k8s backend-gateway backend-image backend-dcm4chee backend-data-task backend-data-dcm backend-data backend-auth backend-algorithm backend-admin algorithm-videosample algorithm-ofrecord algorithm-imgprocess