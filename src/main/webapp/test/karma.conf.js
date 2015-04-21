// Karma configuration
// http://karma-runner.github.io/0.12/config/configuration-file.html
// Generated on 2014-07-17 using
// generator-karma 0.8.3

module.exports = function(config) {
  'use strict';

  config.set({
    // enable / disable watching file and executing tests whenever any file changes
    autoWatch: false,

    // base path, that will be used to resolve files and exclude
    basePath: '../',

    // testing framework to use (jasmine/mocha/qunit/...)
    frameworks: ['jasmine'],


    // list of files / patterns to load in the browser
    files: [
        'bower_components/jquery/dist/jquery.js',
        'app/scripts/thirdparty/fastclick.js',
      'bower_components/angular/angular.js',
    'bower_components/json3/lib/json3.js',
      'bower_components/angular-mocks/angular-mocks.js',
      'bower_components/angular-animate/angular-animate.js',
      'bower_components/angular-cookies/angular-cookies.js',
      'bower_components/angular-resource/angular-resource.js',
      'bower_components/angular-route/angular-route.js',
      'bower_components/angular-sanitize/angular-sanitize.js',
        'bower_components/angular-touch/angular-touch.js',

      'bower_components/bootstrap/dist/js/bootstrap.js',
      'bower_components/angular-ui-router/release/angular-ui-router.js',
      'app/scripts/thirdparty/ui-bootstrap-tpls-0.10.0.js',
      'app/scripts/thirdparty/ui-grid/ui-grid-unstable.min.js',
      'app/scripts/thirdparty/feeds.min.js',
    'app/scripts/thirdparty/charts/jquery.sparkline.min.js',
    'app/scripts/thirdparty/charts/angular.easypiechart.min.js',
    'app/scripts/thirdparty/news-ticker/jquery.breakingNews.js',
    'app/scripts/thirdparty/jquery.easing.min.js',
    'app/scripts/thirdparty/social-share/jquery.floating-share.js',
    'app/scripts/thirdparty/charts/d3.min.js',
    'app/scripts/thirdparty/charts/nv.d3.min.js',
    'app/scripts/thirdparty/charts/angularjs-nvd3-directives.min.js',
    'app/scripts/thirdparty/tour/bootstrap-tour.min.js',
    'app/scripts/thirdparty/counter/jquery.counterup.min.js',
    'app/scripts/thirdparty/counter/waypoints.min.js',
    'app/scripts/thirdparty/file-upload-bootstrap/jasny-bootstrap.min.js',
    'app/scripts/thirdparty/file-upload-bootstrap/angular-file-upload.min.js',
    'app/scripts/vsm.js',
        'app/scripts/common.js',
        'app/scripts/constants/AuthEvents.js',
        'app/scripts/controllers/**/*.js',
        'app/scripts/directives/**/*.js',
        'app/scripts/services/**/*.js',

      'test/mock/**/*.js',
      'test/spec/**/*.js'
    ],

    // list of files / patterns to exclude
    exclude: [],

    // web server port
    port: 8080,

    // Start these browsers, currently available:
    // - Chrome
    // - ChromeCanary
    // - Firefox
    // - Opera
    // - Safari (only Mac)
    // - PhantomJS
    // - IE (only Windows)
    browsers: [
      'PhantomJS'
    ],

    // Which plugins to enable
    plugins: [
      'karma-phantomjs-launcher',
      'karma-jasmine'
    ],

    // Continuous Integration mode
    // if true, it capture browsers, run tests and exit
    singleRun: false,

    colors: true,

    // level of logging
    // possible values: LOG_DISABLE || LOG_ERROR || LOG_WARN || LOG_INFO || LOG_DEBUG
    logLevel: config.LOG_INFO

    // Uncomment the following lines if you are using grunt's server to run the tests
    // proxies: {
    //   '/': 'http://localhost:9000/'
    // },
    // URL root prevent conflicts with the site root
    // urlRoot: '_karma_'
  });
};
