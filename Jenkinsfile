podTemplate(
        cloud: 'OpenShift',
        label: 'selenium-test',
        containers: [
            containerTemplate(
                name: 'jnlp', 
                image: 'jenkins/jnlp-slave:alpine',
                resourceRequestCpu: '100m',
                resourceLimitCpu: '1000m',
                resourceRequestMemory: '100Mi',
                resourceLimitMemory: '1000Mi'
                ),
            containerTemplate(
                name: 'maven-agent',
                image: 'docker.csx.com/devops/jenkins-agent-maven:1.3.0',
                ttyEnabled: true,
                resourceRequestCpu: '100m',
                resourceLimitCpu: '1000m',
                resourceRequestMemory: '100Mi',
                resourceLimitMemory: '2000Mi',
                alwaysPullImage: true,
                envVars: [
                    secretEnvVar(key: 'Z_JENKINS_PWD', secretName: 'jenkins-secrets', secretKey: 'jenkins-password'),
                    secretEnvVar(key: 'Z_JIRA_PWD', secretName: 'jenkins-secrets', secretKey: 'ad-pass'),
            ]
            ),
            containerTemplate(
                name: 'selenium-chrome',
                image: 'docker.csx.com/selenium/standalone-chrome',
                ttyEnabled: true,
                resourceRequestCpu: '100m',
                resourceLimitCpu: '1000m',
                resourceRequestMemory: '100Mi',
                resourceLimitMemory: '1000Mi',
                alwaysPullImage: true,
                envVars: [
                    envVar(key: 'JAVA_OPTS', value: '-Djavax.net.ssl.trustStore=/opt/cacerts/cacerts -Djavax.net.ssl.trustStorePassword=changeit'),
                    envVar(key: 'SE_OPTS', value: '-debug'),
                    envVar(key: 'START_XVFB', value: 'false'),
                    envVar(key: 'TESET', value: 'test')
            ]
            ),
            containerTemplate(
                name: 'selenium-firefox',
                image: 'docker.csx.com/selenium/standalone-firefox',
                ttyEnabled: true,
                resourceRequestCpu: '100m',
                resourceLimitCpu: '1000m',
                resourceRequestMemory: '100Mi',
                resourceLimitMemory: '1000Mi',
                alwaysPullImage: true,
                envVars: [
                    envVar(key: 'JAVA_OPTS', value: '-Djavax.net.ssl.trustStore=/opt/cacerts/cacerts -Djavax.net.ssl.trustStorePassword=changeit'),
                    envVar(key: 'SE_OPTS', value: '-debug'),
                    envVar(key: 'START_XVFB', value: 'false'),
                    envVar(key: 'TESET', value: 'test')
            ]
            )
        ],
      imagePullSecrets: ['docker-auth'],
        volumes: [
            persistentVolumeClaim(mountPath: '/opt/shared/maven/repository', claimName: 'jenkins-storage-maven', readOnly: false),
            secretVolume(mountPath: '/opt/cacerts', secretName: 'go-dev-wildcard')
        ]
    ){
        node('selenium-test') {
            container ('maven-agent'){

                // Discard old build records and keep maximum of 10 build and Disable concurrent builds
                properties([
                parameters([
                string(name: 'mavenTestCommand', description: 'Maven command to run sub modules. For example: mvn test -Dmaven.test.failure.ignore=true -Dheadless=true -DbuildToolRun=true -pl shipcsx-performance-tripplan-test -am. Dont forget to add -am along with -pl.', defaultValue: 'mvn test -Dmaven.test.failure.ignore=true -Dheadless=true -DbuildToolRun=true -Dcucumber.options="--tags @DataExist"'),
                //yet to add implementation for this, idea is to build parallely in case of multiple browsers entered in this field
                string(name: 'browsers', description: 'Browsers to test. For example: chrome,firefox', defaultValue: 'chrome'),
            ]),
                buildDiscarder
                (logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '10', daysToKeepStr: '', numToKeepStr: '10')),
                disableConcurrentBuilds()])
                
                def runParallel = false
                def givenBrowsers = params.browsers
                def buildStages
                def brow

                try {
                  // Validate maven command
                def mvnCmd = params.mavenTestCommand
                
                // need to bunch of other validations too like whether we are right maven commands
                if (!mvnCmd.contains('mvn test')) { 
                    error("************** ${mvnCmd} is not a supported maven command. Failing Build **************")
                }
                
                def browsersArray = givenBrowsers.split(',');
                if(browsersArray.length > 1){
                      runParallel = true;                   
                } else if(browsersArray.length == 1) {
                  brow = browsersArray[0]                                 
                } else {
                  brow = 'chrome'    
                }
                
                  stage('Initialise') {
              // Set up List<Map<String,Closure>> describing the builds
              buildStages = prepareBuildStages(browsersArray, mvnCmd)
              println("Initialised pipeline.")
            }

                    stage('\u2756 Clean Workspace') {
                        deleteDir()
                    }

                    stage('\u2756 Checkout Source') {
                        checkout scm
                    }
                
                    for (builds in buildStages) {
              if (runParallel) {
                parallel(builds)
              } else {
                stage('\u2756 Run tests') {
                    def browserCmd = "-Dbrowser=${brow}"
                            sh "${mvnCmd} ${browserCmd}"
                            archiveArtifacts artifacts: '**/*Report.html'
                        }
              }
            }

                } catch(error){
                    // Need to send failure through email
                    throw error
                }
                finally {
                    cleanWs()
                }
            }
        }
    }
    
 def prepareBuildStages(String[] browsers, String mvnCmd) {  
  def buildStagesList = []
  def buildParallelMap = [:]
  for(i = 0; i < browsers.length; i++) {
    def brow = browsers[i]
    def n = "${brow}"
        buildParallelMap.put(n, prepareOneBuildStage(n, mvnCmd))
  }
  buildStagesList.add(buildParallelMap)
  return buildStagesList
 }
 
 def prepareOneBuildStage(String browser, String mvnCmd) {
  return {
    stage("Build stage:${browser}") {
      println("Building ${browser}")
      def browserCmd = "-Dbrowser=${browser}"
      sh(script: "${mvnCmd} ${browserCmd}", returnStatus:true)
      archiveArtifacts artifacts: 'target/cucumber-html-reports/'
    }
  }
}