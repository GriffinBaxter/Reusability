const sonarqubeScanner =  require('sonarqube-scanner');
sonarqubeScanner(
    {
        serverUrl:  'https://csse-s302g4.canterbury.ac.nz/sonarqube/',
        token: "dfbb0075dfd103401c06e7306086ae841c151b63",
        options : {
            'sonar.projectKey': 'team-400-client',
            'sonar.projectName': 'Team 400 - Client',
            "sonar.sourceEncoding": "UTF-8",
            'sonar.sources': 'src',
            'sonar.tests': 'tests',
            'sonar.inclusions': '**',
            'sonar.test.inclusions': 'src/**/*.spec.js,src/**/*.test.js,src/**/*.test.ts, src/test',
            'sonar.typescript.lcov.reportPaths': 'coverage/lcov.info',
            'sonar.javascript.lcov.reportPaths': 'coverage/lcov.info',
            'sonar.testExecutionReportPaths': 'coverage/test-reporter.xml'
        }
    }, () => {});
