@Library('jenkins-libraries@main') _
pipeline {
    agent {
        label "swarm"
    }

    parameters {
        string defaultValue: '192.168.2.228', name: 'baseUrl', trim: true
        choice choices: ['linux/amd64', 'linux/arm64', 'linux/amd64,linux/arm64'], name: 'platform'
        choice choices: ['nfs-client', 'local-path'], name: 'storageClassName'
        booleanParam defaultValue: false, name: 'skipBuild'
    }

    environment {
        PROJECT_NAME = "dubhe"
        PROJECT_ENV = "system"

        REPOSITORY_URL = "https://github.com/buxiaomo/Dubhe.git"

        REGISTRY_HOST = "192.168.2.228:30002"
        REGISTRY_CREDENTIALS_ID = "harbor"

        // linux/amd64, linux/arm64, linux/amd64,linux/arm64
        IMAGE_platform = "linux/amd64,linux/arm64"
    }

    options {
        disableConcurrentBuilds abortPrevious: true
        timeout(150)
        parallelsAlwaysFailFast()
    }

    stages {
        stage('checkout') {
            steps {
                checkout scmGit(branches: [[name: '*/add-health-check']], extensions: [lfs()], userRemoteConfigs: [[credentialsId: 'github', url: "${env.REPOSITORY_URL}"]])
                withCredentials([usernamePassword(credentialsId: 'harbor', passwordVariable: 'PASSWORD', usernameVariable: 'USERNAME')]) {
                    sh "echo ${PASSWORD} | docker login ${env.REGISTRY_HOST} -u ${USERNAME} --password-stdin"
                }
            }
        }

        stage('compile') {
            when {
                expression { !params.skipBuild }
            }
            parallel {
                stage('webapp') {
                    steps {
                        dir('webapp') {
                            withDockerContainer(image: 'docker.io/library/node:12.22.4', args: '--net host') {
                                sh "sed -i 's/DUBHE_BACKEND_SERVER:8960/${params.baseUrl}:30960/g' .env.production"
                                sh "sed -i 's/DUBHE_MINIO_SERVER/${params.baseUrl}/g' .env.production"
                                sh "sed -i 's/DUBHE_MINIO_PORT/30900/g' .env.production"
                                sh "sed -i 's/DUBHE_WEB_SERVER/${params.baseUrl}:30800/g' .env.production"
                                sh "sed -i 's/DUBHE_ENVIRONMENT/dubhe-prod/g' .env.production"
                                sh "cat .env.production"
                                sh "npm config set registry https://registry.npmmirror.com"
                                sh "npm install"
                                sh "npm run build:prod"
                                sh "ls -l"
                            }
                        }
                    }
                }
                stage('distribute-train-operator') {
                    steps {
                        dir('distribute-train-operator') {
                            withDockerContainer(image: 'docker.io/library/maven:3.5-jdk-8', args: '--net host -v m2:/root/.m2') {
                                sh "mvn clean compile package -Dmaven.test.skip=true -Dmaven.compile.fork=true"
                            }
                        }
                    }
                }
                stage('dubhe-server') {
                    steps {
                        dir('dubhe-server') {
                            withDockerContainer(image: 'docker.io/library/maven:3.5-jdk-8', args: '--net host -v m2:/root/.m2') {
                                sh "mvn clean compile package -Dmaven.test.skip=true -Dmaven.compile.fork=true"
                            }
                        }
                    }
                }
            }
        }

        stage('build image') {
            when {
                expression { !params.skipBuild }
            }
            parallel {
                stage('webapp') {
                    steps {
                        dir('webapp') {
                            script{
                                BuildDockerImage(this) {
                                    name = 'web'
                                    platform = "linux/amd64,linux/arm64"
                                    path = './Dockerfile'
                                    buildArgs = [
                                        "REGISTRY_HOST=192.168.2.228:30002"
                                    ]
                                }
                            }
                        }
                    }
                }

                stage('serving-gpu') {
                    steps {
                        dir('tianshu_serving') {
                            script{
                                BuildDockerImage(this) {
                                    name = 'serving-gpu'
                                    tag = 'base'
                                    platform = "linux/amd64,linux/arm64"
                                    path = './Dockerfile'
                                    buildArgs = [
                                        "REGISTRY_HOST=192.168.2.228:30002"
                                    ]
                                }
                            }
                        }
                    }
                }

                stage('dubhe-storage') {
                    steps {
                        dir('dubhe-storage') {
                            script{
                                BuildDockerImage(this) {
                                    name = 'storage-init'
                                    platform = "linux/amd64,linux/arm64"
                                    path = './Dockerfile'
                                    buildArgs = [
                                        "REGISTRY_HOST=192.168.2.228:30002"
                                    ]
                                }
                            }
                        }
                    }
                }
                stage('dubhe-data-process') {
                    steps {
                        dir('dubhe_data_process') {
                            script{
                                BuildDockerImage(this) {
                                    name = 'dubhe-data-process'
                                    platform = "linux/amd64,linux/arm64"
                                    path = './Dockerfile'
                                    buildArgs = [
                                        "REGISTRY_HOST=192.168.2.228:30002"
                                    ]
                                }
                            }
                        }
                    }
                }
                stage('model-converter') {
                    steps {
                        dir('model-converter') {
                            script{
                                BuildDockerImage(this) {
                                    name = 'model-converter'
                                    platform = "linux/amd64,linux/arm64"
                                    path = './Dockerfile'
                                    buildArgs = [
                                        "REGISTRY_HOST=192.168.2.228:30002"
                                    ]
                                }
                            }
                        }
                    }
                }
                stage('model-measuring') {
                    steps {
                        dir('model_measuring') {
                            script{
                                BuildDockerImage(this) {
                                    name = 'model-measuring'
                                    platform = "linux/amd64"
                                    path = './Dockerfile'
                                    buildArgs = [
                                        "REGISTRY_HOST=192.168.2.228:30002"
                                    ]
                                }
                            }
                        }
                    }
                }
                stage('ofrecord') {
                    steps {
                        dir('dubhe_data_process/docker-image/ofrecord') {
                            script{
                                BuildDockerImage(this) {
                                    name = 'algorithm-ofrecord'
                                    platform = "linux/amd64"
                                    path = './Dockerfile'
                                    buildArgs = [
                                        "REGISTRY_HOST=192.168.2.228:30002"
                                    ]
                                }
                            }
                        }
                    }
                }
                stage('database') {
                    steps {
                        dir('dubhe-server/sql') {
                            script{
                                BuildDockerImage(this) {
                                    name = 'mysql'
                                    platform = "linux/amd64,linux/arm64"
                                    path = './Dockerfile'
                                    buildArgs = [
                                        "REGISTRY_HOST=192.168.2.228:30002"
                                    ]
                                }
                            }
                        }
                    }
                }
                stage('admin') {
                    steps {
                        dir('dubhe-server/admin') {
                            script{
                                BuildDockerImage(this) {
                                    name = 'admin'
                                    platform = "linux/amd64,linux/arm64"
                                    path = './Dockerfile'
                                    buildArgs = [
                                        "REGISTRY_HOST=192.168.2.228:30002"
                                    ]
                                }
                            }
                        }
                    }
                }
                stage('auth') {
                    steps {
                        dir('dubhe-server/auth') {
                            script{
                                BuildDockerImage(this) {
                                    name = 'auth'
                                    platform = "linux/amd64,linux/arm64"
                                    path = './Dockerfile'
                                    buildArgs = [
                                        "REGISTRY_HOST=192.168.2.228:30002"
                                    ]
                                }
                            }
                        }
                    }
                }
                stage('gateway') {
                    steps {
                        dir('dubhe-server/gateway') {
                            script{
                                BuildDockerImage(this) {
                                    name = 'gateway'
                                    platform = "linux/amd64,linux/arm64"
                                    path = './Dockerfile'
                                    buildArgs = [
                                        "REGISTRY_HOST=192.168.2.228:30002"
                                    ]
                                }
                            }
                        }
                    }
                }
                stage('dubhe-train') {
                    steps {
                        dir('dubhe-server/dubhe-train') {
                            script{
                                BuildDockerImage(this) {
                                    name = 'dubhe-train'
                                    platform = "linux/amd64,linux/arm64"
                                    path = './Dockerfile'
                                    buildArgs = [
                                        "REGISTRY_HOST=192.168.2.228:30002"
                                    ]
                                }
                            }
                        }
                    }
                }
                stage('dubhe-algorithm') {
                    steps {
                        dir('dubhe-server/dubhe-algorithm') {
                            script{
                                BuildDockerImage(this) {
                                    name = 'dubhe-algorithm'
                                    platform = "linux/amd64,linux/arm64"
                                    path = './Dockerfile'
                                    buildArgs = [
                                        "REGISTRY_HOST=192.168.2.228:30002"
                                    ]
                                }
                            }
                        }
                    }
                }
                stage('dubhe-data') {
                    steps {
                        dir('dubhe-server/dubhe-data') {
                            script{
                                BuildDockerImage(this) {
                                    name = 'dubhe-data'
                                    platform = "linux/amd64,linux/arm64"
                                    path = './Dockerfile'
                                    buildArgs = [
                                        "REGISTRY_HOST=192.168.2.228:30002"
                                    ]
                                }
                            }
                        }
                    }
                }
                stage('dubhe-data-dcm') {
                    steps {
                        dir('dubhe-server/dubhe-data-dcm') {
                            script{
                                BuildDockerImage(this) {
                                    name = 'dubhe-data-dcm'
                                    platform = "linux/amd64,linux/arm64"
                                    path = './Dockerfile'
                                    buildArgs = [
                                        "REGISTRY_HOST=192.168.2.228:30002"
                                    ]
                                }
                            }
                        }
                    }
                }
                stage('dubhe-data-task') {
                    steps {
                        dir('dubhe-server/dubhe-data-task') {
                            script{
                                BuildDockerImage(this) {
                                    name = 'dubhe-data-task'
                                    platform = "linux/amd64,linux/arm64"
                                    path = './Dockerfile'
                                    buildArgs = [
                                        "REGISTRY_HOST=192.168.2.228:30002"
                                    ]                                    
                                }
                            }
                        }
                    }
                }
                stage('dubhe-image') {
                    steps {
                        dir('dubhe-server/dubhe-image') {
                            script{
                                BuildDockerImage(this) {
                                    name = 'dubhe-image'
                                    platform = "linux/amd64,linux/arm64"
                                    path = './Dockerfile'
                                    buildArgs = [
                                        "REGISTRY_HOST=192.168.2.228:30002"
                                    ]
                                }
                            }
                        }
                    }
                }
                stage('dubhe-k8s') {
                    steps {
                        dir('dubhe-server/dubhe-k8s') {
                            script{
                                BuildDockerImage(this) {
                                    name = 'dubhe-k8s'
                                    platform = "linux/amd64,linux/arm64"
                                    path = './Dockerfile'
                                    buildArgs = [
                                        "REGISTRY_HOST=192.168.2.228:30002"
                                    ]
                                }
                            }
                        }
                    }
                }
                stage('dubhe-measure') {
                    steps {
                        dir('dubhe-server/dubhe-measure') {
                            script{
                                BuildDockerImage(this) {
                                    name = 'dubhe-measure'
                                    platform = "linux/amd64,linux/arm64"
                                    path = './Dockerfile'
                                    buildArgs = [
                                        "REGISTRY_HOST=192.168.2.228:30002"
                                    ]
                                }
                            }
                        }
                    }
                }
                stage('dubhe-model') {
                    steps {
                        dir('dubhe-server/dubhe-model') {
                            script{
                                BuildDockerImage(this) {
                                    name = 'dubhe-model'
                                    platform = "linux/amd64,linux/arm64"
                                    path = './Dockerfile'
                                    buildArgs = [
                                        "REGISTRY_HOST=192.168.2.228:30002"
                                    ]
                                }
                            }
                        }
                    }
                }
                stage('dubhe-notebook') {
                    steps {
                        dir('dubhe-server/dubhe-notebook') {
                            script{
                                BuildDockerImage(this) {
                                    name = 'dubhe-notebook'
                                    platform = "linux/amd64,linux/arm64"
                                    path = './Dockerfile'
                                    buildArgs = [
                                        "REGISTRY_HOST=192.168.2.228:30002"
                                    ]
                                }
                            }
                        }
                    }
                }
                stage('dubhe-optimize') {
                    steps {
                        dir('dubhe-server/dubhe-optimize') {
                            script{
                                BuildDockerImage(this) {
                                    name = 'dubhe-optimize'
                                    platform = "linux/amd64,linux/arm64"
                                    path = './Dockerfile'
                                    buildArgs = [
                                        "REGISTRY_HOST=192.168.2.228:30002"
                                    ]
                                }
                            }
                        }
                    }
                }
                stage('dubhe-visual-server') {
                    steps {
                        dir('dubhe-visual-server') {
                            script{
                                BuildDockerImage(this) {
                                    name = 'dubhe-visual-server'
                                    platform = "linux/amd64"
                                    path = './Dockerfile'
                                    buildArgs = [
                                        "REGISTRY_HOST=192.168.2.228:30002"
                                    ]
                                }
                            }
                        }
                    }
                }
                stage('dubhe-point-cloud') {
                    steps {
                        dir('dubhe-server/dubhe-point-cloud') {
                            script{
                                BuildDockerImage(this) {
                                    name = 'dubhe-point-cloud'
                                    platform = "linux/amd64,linux/arm64"
                                    path = './Dockerfile'
                                    buildArgs = [
                                        "REGISTRY_HOST=192.168.2.228:30002"
                                    ]
                                }
                            }
                        }
                    }
                }
                stage('dubhe-serving') {
                    steps {
                        dir('dubhe-server/dubhe-serving') {
                            script{
                                BuildDockerImage(this) {
                                    name = 'dubhe-serving'
                                    platform = "linux/amd64,linux/arm64"
                                    path = './Dockerfile'
                                    buildArgs = [
                                        "REGISTRY_HOST=192.168.2.228:30002"
                                    ]
                                }
                            }
                        }
                    }
                }
                stage('dubhe-serving-gateway') {
                    steps {
                        dir('dubhe-server/dubhe-serving-gateway') {
                            script{
                                BuildDockerImage(this) {
                                    name = 'dubhe-serving-gateway'
                                    platform = "linux/amd64,linux/arm64"
                                    path = './Dockerfile'
                                    buildArgs = [
                                        "REGISTRY_HOST=192.168.2.228:30002"
                                    ]
                                }
                            }
                        }
                    }
                }
                stage('dubhe-tadl') {
                    steps {
                        dir('dubhe-server/dubhe-tadl') {
                            script{
                                BuildDockerImage(this) {
                                    name = 'dubhe-tadl'
                                    platform = "linux/amd64,linux/arm64"
                                    path = './Dockerfile'
                                    buildArgs = [
                                        "REGISTRY_HOST=192.168.2.228:30002"
                                    ]
                                }
                            }
                        }
                    }
                }
                stage('dubhe-terminal') {
                    steps {
                        dir('dubhe-server/dubhe-terminal') {
                            script{
                                BuildDockerImage(this) {
                                    name = 'dubhe-terminal'
                                    platform = "linux/amd64,linux/arm64"
                                    path = './Dockerfile'
                                    buildArgs = [
                                        "REGISTRY_HOST=192.168.2.228:30002"
                                    ]
                                }
                            }
                        }
                    }
                }
                stage('notebook') {
                    steps {
                        dir('notebook/12.6') {
                            script{
                                BuildDockerImage(this) {
                                    name = 'notebook'
                                    tag = '12.6.3-cudnn-devel-ubuntu20.04'
                                    platform = "linux/amd64,linux/arm64"
                                    path = './Dockerfile'
                                    buildArgs = [
                                        "REGISTRY_HOST=192.168.2.228:30002"
                                    ]
                                }
                            }
                        }
                    }
                }
                stage('sshd') {
                    steps {
                        dir('sshd') {
                            script{
                                BuildDockerImage(this) {
                                    name = 'sshd'
                                    tag = '10.0'
                                    platform = "linux/amd64,linux/arm64"
                                    path = './Dockerfile'
                                    buildArgs = [
                                        "REGISTRY_HOST=192.168.2.228:30002"
                                    ]
                                }
                            }
                        }
                    }
                }
                stage('distribute-train-operator') {
                    steps {
                        dir('distribute-train-operator') {
                            script{
                                BuildDockerImage(this) {
                                    name = 'distribute-train-operator'
                                    platform = "linux/amd64,linux/arm64"
                                    path = './Dockerfile'
                                    buildArgs = [
                                        "REGISTRY_HOST=192.168.2.228:30002"
                                    ]
                                }
                            }
                        }
                    }
                }
            }
        }

        stage('deploy') {
            steps {
                script {
                    def proceed = true
                    try {
                        input message: '确认部署吗？', ok: '确认'
                    } catch (Exception e) {
                        echo "用户取消了部署"
                        proceed = false
                        currentBuild.result = 'ABORTED'
                    }
                    if (proceed) {
                        // checkout scmGit(branches: [[name: '*/main']], extensions: [[$class: 'RelativeTargetDirectory', relativeTargetDir: 'dubhe-chart']], userRemoteConfigs: [[url: "https://github.com/buxiaomo/dubhe-chart.git"]])
                        sh "cp /etc/kubernetes/remote-access.kubeconfig ./helm-chart/files/admin.kubeconfig"
                        if (params.skipBuild) {
                            def projectName = env.PROJECT_NAME
                            def projectEnv = env.PROJECT_ENV
                            def registryHost = env.REGISTRY_HOST
                            def jobName = env.JOB_NAME
                            def storageClassName  = params.storageClassName
                            HelmDeploy(this) {
                                name = 'dubhe'
                                namespace = "${projectName}-${projectEnv}"
                                path = './helm-chart'
                                set = [
                                    "global.storageClassName=${storageClassName}",
                                    "dubhe.cicd.enabled=false",
                                    "dubhe.image.host=${registryHost}",
                                    "dubhe.image.repository=${jobName}",
                                    "dubhe.image.tag=latest",
                                    "redis.image.repository=192.168.2.228:30002/library/redis",
                                    "minio.image.repository=192.168.2.228:30002/minio/minio",
                                    "minio.mc.image.repository=192.168.2.228:30002/minio/mc",
                                    "nacos.image.repository=192.168.2.228:30002/nacos/nacos-server",
                                    "nacos.mysql.image.repository=192.168.2.228:30002/nacos/nacos-mysql",
                                    "monitoring.initContainers.infra.image.repository=192.168.2.228:30002/library/alpine",
                                    "monitoring.nodexporter.image.repository=192.168.2.228:30002/prom/node-exporter",
                                    "monitoring.prometheus.image.repository=192.168.2.228:30002/prom/prometheus",
                                    "monitoring.grafana.image.repository=192.168.2.228:30002/grafana/grafana",
                                    "log.initContainers.infra.image.repository=192.168.2.228:30002/library/alpine",
                                    "log.elasticsearch.infra.image.repository=192.168.2.228:30002/elasticsearch/elasticsearch",
                                    "log.fluentbit.infra.image.repository=192.168.2.228:30002/fluent/fluent-bit",
                                    "dubhe.initContainers.infra.image.repository=192.168.2.228:30002/library/busybox"
                                ]
                            }
                        } else {
                            def projectName = env.PROJECT_NAME
                            def projectEnv = env.PROJECT_ENV
                            def registryHost = env.REGISTRY_HOST
                            def jobName = env.JOB_NAME
                            def storageClassName  = params.storageClassName
                            def tag = env.BUILD_NUMBER
                            HelmDeploy(this) {
                                name = 'dubhe'
                                namespace = "${projectName}-${projectEnv}"
                                path = './helm-chart'
                                set = [
                                    "global.storageClassName=${storageClassName}",
                                    "dubhe.image.host=${registryHost}",
                                    "dubhe.image.repository=${jobName}",
                                    "dubhe.image.tag=${tag}",
                                    "redis.image.repository=192.168.2.228:30002/library/redis",
                                    "minio.image.repository=192.168.2.228:30002/minio/minio",
                                    "minio.mc.image.repository=192.168.2.228:30002/minio/mc",
                                    "nacos.image.repository=192.168.2.228:30002/nacos/nacos-server",
                                    "nacos.mysql.image.repository=192.168.2.228:30002/nacos/nacos-mysql",
                                    "monitoring.initContainers.infra.image.repository=192.168.2.228:30002/library/alpine",
                                    "monitoring.nodexporter.image.repository=192.168.2.228:30002/prom/node-exporter",
                                    "monitoring.prometheus.image.repository=192.168.2.228:30002/prom/prometheus",
                                    "monitoring.grafana.image.repository=192.168.2.228:30002/grafana/grafana",
                                    "log.initContainers.infra.image.repository=192.168.2.228:30002/library/alpine",
                                    "log.elasticsearch.infra.image.repository=192.168.2.228:30002/elasticsearch/elasticsearch",
                                    "log.fluentbit.infra.image.repository=192.168.2.228:30002/fluent/fluent-bit",
                                    "dubhe.initContainers.infra.image.repository=192.168.2.228:30002/library/busybox"
                                ]
                            }
                        }
                    }
                }
            }
        }
    }
}