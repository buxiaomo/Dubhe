pipeline {
    agent { 
        label "swarm"
    }

    environment {
        PROJECT_NAME = "dubhe"
        PROJECT_ENV = "system"

        REPOSITORY_URL = "https://gitee.com/buxiaomo/Dubhe.git"

        REGISTRY_HOST = "192.168.1.2:5000"
    }


    options {
        disableConcurrentBuilds abortPrevious: true
    }

    stages {
        stage('checkout') {
            steps {
                checkout scmGit(branches: [[name: '*/buxiaomo']], extensions: [], userRemoteConfigs: [[url: "${env.REPOSITORY_URL}"]])
            }
        }

        stage('compile') {
            steps {
                dir('dubhe-server') {
                    withDockerContainer(image: 'maven:3.5-jdk-8', args: '--net host -v m2:/root/.m2') {
                        sh "mvn clean compile package -T 1C -Dmaven.test.skip=true -Dmaven.compile.fork=true"
                    }
                }
            }
        }

        stage('build image') {
            parallel {
                stage('python') {
                    steps {
                        dir('dubhe_data_process') {
                            sh label: 'build image', script: "docker buildx build --platform=amd64,arm64 -t ${env.REGISTRY_HOST}/${env.PROJECT_NAME}/python:v1 --push ."
                        }
                    }
                }
                stage('admin') {
                    steps {
                        dir('dubhe-server/admin') {
                            sh label: 'build image', script: "docker buildx build --platform=amd64,arm64 -t ${env.REGISTRY_HOST}/${env.PROJECT_NAME}/admin:v1 --push ."
                        }
                    }
                }
                stage('auth') {
                    steps {
                        dir('dubhe-server/auth') {
                            sh label: 'build image', script: "docker buildx build --platform=amd64,arm64 -t ${env.REGISTRY_HOST}/${env.PROJECT_NAME}/auth:v1 --push ."
                        }
                    }
                }
                stage('gateway') {
                    steps {
                        dir('dubhe-server/gateway') {
                            sh label: 'build image', script: "docker buildx build --platform=amd64,arm64 -t ${env.REGISTRY_HOST}/${env.PROJECT_NAME}/gateway:v1 --push ."
                        }
                    }
                }
                stage('dubhe-train') {
                    steps {
                        dir('dubhe-server/dubhe-train') {
                            sh label: 'build image', script: "docker buildx build --platform=amd64,arm64 -t ${env.REGISTRY_HOST}/${env.PROJECT_NAME}/dubhe-train:v1 --push ."
                        }
                    }
                }
                stage('dubhe-algorithm') {
                    steps {
                        dir('dubhe-server/dubhe-algorithm') {
                            sh label: 'build image', script: "docker buildx build --platform=amd64,arm64 -t ${env.REGISTRY_HOST}/${env.PROJECT_NAME}/dubhe-algorithm:v1 --push ."
                        }
                    }
                }
                stage('dubhe-data') {
                    steps {
                        dir('dubhe-server/dubhe-data') {
                            sh label: 'build image', script: "docker buildx build --platform=amd64,arm64 -t ${env.REGISTRY_HOST}/${env.PROJECT_NAME}/dubhe-data:v1 --push ."
                        }
                    }
                }
                stage('dubhe-data-dcm') {
                    steps {
                        dir('dubhe-server/dubhe-data-dcm') {
                            sh label: 'build image', script: "docker buildx build --platform=amd64,arm64 -t ${env.REGISTRY_HOST}/${env.PROJECT_NAME}/dubhe-data-dcm:v1 --push ."
                        }
                    }
                }
                stage('dubhe-data-task') {
                    steps {
                        dir('dubhe-server/dubhe-data-task') {
                            sh label: 'build image', script: "docker buildx build --platform=amd64,arm64 -t ${env.REGISTRY_HOST}/${env.PROJECT_NAME}/dubhe-data-task:v1 --push ."
                        }
                    }
                }
                stage('dubhe-image') {
                    steps {
                        dir('dubhe-server/dubhe-image') {
                            sh label: 'build image', script: "docker buildx build --platform=amd64,arm64 -t ${env.REGISTRY_HOST}/${env.PROJECT_NAME}/dubhe-image:v1 --push ."
                        }
                    }
                }
                stage('dubhe-k8s') {
                    steps {
                        dir('dubhe-server/dubhe-k8s') {
                            sh label: 'build image', script: "docker buildx build --platform=amd64,arm64 -t ${env.REGISTRY_HOST}/${env.PROJECT_NAME}/dubhe-k8s:v1 --push ."
                        }
                    }
                }
                stage('dubhe-measure') {
                    steps {
                        dir('dubhe-server/dubhe-measure') {
                            sh label: 'build image', script: "docker buildx build --platform=amd64,arm64 -t ${env.REGISTRY_HOST}/${env.PROJECT_NAME}/dubhe-measure:v1 --push ."
                        }
                    }
                }
                stage('dubhe-model') {
                    steps {
                        dir('dubhe-server/dubhe-model') {
                            sh label: 'build image', script: "docker buildx build --platform=amd64,arm64 -t ${env.REGISTRY_HOST}/${env.PROJECT_NAME}/dubhe-model:v1 --push ."
                        }
                    }
                }
                stage('dubhe-notebook') {
                    steps {
                        dir('dubhe-server/dubhe-notebook') {
                            sh label: 'build image', script: "docker buildx build --platform=amd64,arm64 -t ${env.REGISTRY_HOST}/${env.PROJECT_NAME}/dubhe-notebook:v1 --push ."
                        }
                    }
                }
                stage('dubhe-optimize') {
                    steps {
                        dir('dubhe-server/dubhe-optimize') {
                            sh label: 'build image', script: "docker buildx build --platform=amd64,arm64 -t ${env.REGISTRY_HOST}/${env.PROJECT_NAME}/dubhe-optimize:v1 --push ."
                        }
                    }
                }
                stage('dubhe-point-cloud') {
                    steps {
                        dir('dubhe-server/dubhe-point-cloud') {
                            sh label: 'build image', script: "docker buildx build --platform=amd64,arm64 -t ${env.REGISTRY_HOST}/${env.PROJECT_NAME}/dubhe-point-cloud:v1 --push ."
                        }
                    }
                }
                stage('dubhe-serving') {
                    steps {
                        dir('dubhe-server/dubhe-serving') {
                            sh label: 'build image', script: "docker buildx build --platform=amd64,arm64 -t ${env.REGISTRY_HOST}/${env.PROJECT_NAME}/dubhe-serving:v1 --push ."
                        }
                    }
                }
                stage('dubhe-serving-gateway') {
                    steps {
                        dir('dubhe-server/dubhe-serving-gateway') {
                            sh label: 'build image', script: "docker buildx build --platform=amd64,arm64 -t ${env.REGISTRY_HOST}/${env.PROJECT_NAME}/dubhe-serving-gateway:v1 --push ."
                        }
                    }
                }
                stage('dubhe-tadl') {
                    steps {
                        dir('dubhe-server/dubhe-tadl') {
                            sh label: 'build image', script: "docker buildx build --platform=amd64,arm64 -t ${env.REGISTRY_HOST}/${env.PROJECT_NAME}/dubhe-tadl:v1 --push ."
                        }
                    }
                }
                stage('dubhe-terminal') {
                    steps {
                        dir('dubhe-server/dubhe-terminal') {
                            sh label: 'build image', script: "docker buildx build --platform=amd64,arm64 -t ${env.REGISTRY_HOST}/${env.PROJECT_NAME}/dubhe-terminal:v1 --push ."
                        }
                    }
                }
            }
        }
    }
}