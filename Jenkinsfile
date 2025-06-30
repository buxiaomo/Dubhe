pipeline {
    agent { 
        label "swarm"
    }

    parameters {
        string defaultValue: '192.168.2.228', name: 'baseUrl', trim: true
        choice choices: ['amd64', 'arm64', 'amd64,arm64'], name: 'platform'
        choice choices: ['local-path', 'nfs-client'], name: 'storageClassName'
    }

    environment {
        PROJECT_NAME = "dubhe"
        PROJECT_ENV = "system"

        REPOSITORY_URL = "https://github.com/buxiaomo/Dubhe.git"

        REGISTRY_HOST = "192.168.2.228:30002"
    }

    options {
        disableConcurrentBuilds abortPrevious: true
        timeout(120)
    }

    stages {
        stage('checkout') {
            steps {
                checkout scmGit(branches: [[name: '*/add-health-check']], extensions: [lfs()], userRemoteConfigs: [[url: "${env.REPOSITORY_URL}"]])
                withCredentials([usernamePassword(credentialsId: 'harbor', passwordVariable: 'PASSWORD', usernameVariable: 'USERNAME')]) {
                    sh "echo ${PASSWORD} | docker login ${env.REGISTRY_HOST} -u ${USERNAME} --password-stdin"
                }
            }
        }

        stage('compile') {
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
            parallel {
                stage('webapp') {
                    steps {
                        dir('webapp') {
                            sh label: 'build image', script: "docker buildx build --platform=${params.platform} -t ${env.REGISTRY_HOST}/${env.JOB_NAME}/web:${BUILD_NUMBER} --push ."
                        }
                    }
                }

                stage('dubhe-storage') {
                    steps {
                        dir('dubhe-storage') {
                            sh label: 'build image', script: "docker buildx build --platform=${params.platform} -t ${env.REGISTRY_HOST}/${env.JOB_NAME}/storage-init:${BUILD_NUMBER} --push ."
                        }
                    }
                }
                stage('dubhe-data-process') {
                    steps {
                        dir('dubhe_data_process') {
                            sh label: 'build image', script: "docker buildx build --platform=${params.platform} -t ${env.REGISTRY_HOST}/${env.JOB_NAME}/dubhe-data-process:${BUILD_NUMBER} --push ."
                        }
                    }
                }
                stage('ofrecord') {
                    steps {
                        dir('dubhe_data_process/docker-image/ofrecord') {
                            sh label: 'build image', script: "docker buildx build --platform=${params.platform} -t ${env.REGISTRY_HOST}/${env.JOB_NAME}/algorithm-ofrecord:${BUILD_NUMBER} --push ."
                        }
                    }
                }
                stage('database') {
                    steps {
                        dir('dubhe-server/sql') {
                            sh label: 'build image', script: "docker buildx build --platform=${params.platform} -t ${env.REGISTRY_HOST}/${env.JOB_NAME}/mysql:${BUILD_NUMBER} --push ."
                        }
                    }
                }
                // stage('model-converter') {
                //     steps {
                //         dir('model-converter') {
                //             sh label: 'build image', script: "docker buildx build --platform=${params.platform} -t ${env.REGISTRY_HOST}/${env.JOB_NAME}/model-converter:${BUILD_NUMBER} --push ."
                //         }
                //     }
                // }
                // stage('model-measuring') {
                //     steps {
                //         dir('model_measuring') {
                //             sh label: 'build image', script: "docker buildx build --platform=${params.platform} -t ${env.REGISTRY_HOST}/${env.JOB_NAME}/model-measuring:${BUILD_NUMBER} --push ."
                //         }
                //     }
                // }
                stage('admin') {
                    steps {
                        dir('dubhe-server/admin') {
                            sh label: 'build image', script: "docker buildx build --platform=${params.platform} -t ${env.REGISTRY_HOST}/${env.JOB_NAME}/admin:${BUILD_NUMBER} --push ."
                        }
                    }
                }
                stage('auth') {
                    steps {
                        dir('dubhe-server/auth') {
                            sh label: 'build image', script: "docker buildx build --platform=${params.platform} -t ${env.REGISTRY_HOST}/${env.JOB_NAME}/auth:${BUILD_NUMBER} --push ."
                        }
                    }
                }
                stage('gateway') {
                    steps {
                        dir('dubhe-server/gateway') {
                            sh label: 'build image', script: "docker buildx build --platform=${params.platform} -t ${env.REGISTRY_HOST}/${env.JOB_NAME}/gateway:${BUILD_NUMBER} --push ."
                        }
                    }
                }
                stage('dubhe-train') {
                    steps {
                        dir('dubhe-server/dubhe-train') {
                            sh label: 'build image', script: "docker buildx build --platform=${params.platform} -t ${env.REGISTRY_HOST}/${env.JOB_NAME}/dubhe-train:${BUILD_NUMBER} --push ."
                        }
                    }
                }
                stage('dubhe-algorithm') {
                    steps {
                        dir('dubhe-server/dubhe-algorithm') {
                            sh label: 'build image', script: "docker buildx build --platform=${params.platform} -t ${env.REGISTRY_HOST}/${env.JOB_NAME}/dubhe-algorithm:${BUILD_NUMBER} --push ."
                        }
                    }
                }
                stage('dubhe-data') {
                    steps {
                        dir('dubhe-server/dubhe-data') {
                            sh label: 'build image', script: "docker buildx build --platform=${params.platform} -t ${env.REGISTRY_HOST}/${env.JOB_NAME}/dubhe-data:${BUILD_NUMBER} --push ."
                        }
                    }
                }
                stage('dubhe-data-dcm') {
                    steps {
                        dir('dubhe-server/dubhe-data-dcm') {
                            sh label: 'build image', script: "docker buildx build --platform=${params.platform} -t ${env.REGISTRY_HOST}/${env.JOB_NAME}/dubhe-data-dcm:${BUILD_NUMBER} --push ."
                        }
                    }
                }
                stage('dubhe-data-task') {
                    steps {
                        dir('dubhe-server/dubhe-data-task') {
                            sh label: 'build image', script: "docker buildx build --platform=${params.platform} -t ${env.REGISTRY_HOST}/${env.JOB_NAME}/dubhe-data-task:${BUILD_NUMBER} --push ."
                        }
                    }
                }
                stage('dubhe-image') {
                    steps {
                        dir('dubhe-server/dubhe-image') {
                            sh label: 'build image', script: "docker buildx build --platform=${params.platform} -t ${env.REGISTRY_HOST}/${env.JOB_NAME}/dubhe-image:${BUILD_NUMBER} --push ."
                        }
                    }
                }
                stage('dubhe-k8s') {
                    steps {
                        dir('dubhe-server/dubhe-k8s') {
                            sh label: 'build image', script: "docker buildx build --platform=${params.platform} -t ${env.REGISTRY_HOST}/${env.JOB_NAME}/dubhe-k8s:${BUILD_NUMBER} --push ."
                        }
                    }
                }
                stage('dubhe-measure') {
                    steps {
                        dir('dubhe-server/dubhe-measure') {
                            sh label: 'build image', script: "docker buildx build --platform=${params.platform} -t ${env.REGISTRY_HOST}/${env.JOB_NAME}/dubhe-measure:${BUILD_NUMBER} --push ."
                        }
                    }
                }
                stage('dubhe-model') {
                    steps {
                        dir('dubhe-server/dubhe-model') {
                            sh label: 'build image', script: "docker buildx build --platform=${params.platform} -t ${env.REGISTRY_HOST}/${env.JOB_NAME}/dubhe-model:${BUILD_NUMBER} --push ."
                        }
                    }
                }
                stage('dubhe-notebook') {
                    steps {
                        dir('dubhe-server/dubhe-notebook') {
                            sh label: 'build image', script: "docker buildx build --platform=${params.platform} -t ${env.REGISTRY_HOST}/${env.JOB_NAME}/dubhe-notebook:${BUILD_NUMBER} --push ."
                        }
                    }
                }
                stage('dubhe-optimize') {
                    steps {
                        dir('dubhe-server/dubhe-optimize') {
                            sh label: 'build image', script: "docker buildx build --platform=${params.platform} -t ${env.REGISTRY_HOST}/${env.JOB_NAME}/dubhe-optimize:${BUILD_NUMBER} --push ."
                        }
                    }
                }
                stage('dubhe-point-cloud') {
                    steps {
                        dir('dubhe-server/dubhe-point-cloud') {
                            sh label: 'build image', script: "docker buildx build --platform=${params.platform} -t ${env.REGISTRY_HOST}/${env.JOB_NAME}/dubhe-point-cloud:${BUILD_NUMBER} --push ."
                        }
                    }
                }
                stage('dubhe-serving') {
                    steps {
                        dir('dubhe-server/dubhe-serving') {
                            sh label: 'build image', script: "docker buildx build --platform=${params.platform} -t ${env.REGISTRY_HOST}/${env.JOB_NAME}/dubhe-serving:${BUILD_NUMBER} --push ."
                        }
                    }
                }
                stage('dubhe-serving-gateway') {
                    steps {
                        dir('dubhe-server/dubhe-serving-gateway') {
                            sh label: 'build image', script: "docker buildx build --platform=${params.platform} -t ${env.REGISTRY_HOST}/${env.JOB_NAME}/dubhe-serving-gateway:${BUILD_NUMBER} --push ."
                        }
                    }
                }
                stage('dubhe-tadl') {
                    steps {
                        dir('dubhe-server/dubhe-tadl') {
                            sh label: 'build image', script: "docker buildx build --platform=${params.platform} -t ${env.REGISTRY_HOST}/${env.JOB_NAME}/dubhe-tadl:${BUILD_NUMBER} --push ."
                        }
                    }
                }
                stage('dubhe-terminal') {
                    steps {
                        dir('dubhe-server/dubhe-terminal') {
                            sh label: 'build image', script: "docker buildx build --platform=${params.platform} -t ${env.REGISTRY_HOST}/${env.JOB_NAME}/dubhe-terminal:${BUILD_NUMBER} --push ."
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
                        checkout scmGit(branches: [[name: '*/main']], extensions: [[$class: 'RelativeTargetDirectory', relativeTargetDir: 'dubhe-chart']], userRemoteConfigs: [[url: "https://github.com/buxiaomo/dubhe-chart.git"]])
                        sh "helm upgrade -i dubhe ./dubhe-chart -n ${env.PROJECT_NAME}-${env.PROJECT_ENV} --set global.storageClassName=${params.storageClassName} --set dubhe.cicd.enabled=false --set dubhe.image.host=${env.REGISTRY_HOST} --set dubhe.image.repository=${env.JOB_NAME} --set dubhe.image.tag=${BUILD_NUMBER} --wait --timeout 1h0s"
                    }
                }
            }
        }
    }
}