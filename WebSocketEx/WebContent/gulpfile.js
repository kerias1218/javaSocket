//gulp 모듈 포함
var gulp = require('gulp');

//플러그인 포함
var postcss = require('gulp-postcss');     //postcss 모듈
var postcss_import = require('postcss-import');    //postcss import 처리 플러그인
var autoprefixer = require('autoprefixer');         //postcss prefix 처리 플러그인     
var postcss_simple_vars = require('postcss-simple-vars');     //postcss sass형식 변수 지원 플러그인  
var postcss_nested = require('postcss-nested');                 //postcss nest 문법 지원 플러그인
var cssnano = require('cssnano');                         //postcss 압축 플러그인

var sourcemaps = require('gulp-sourcemaps'); //소스파일 원본 위치 기록

var browser_sync = require('browser-sync').create() //browser-sync

//경로및 파일 지정
var src = {
    html: './**/*.jsp',
    js: './js/*.js',
    css: './css/src/**/*',
    style: './css/src/style.css'
}
//출력경로 설정
var dest = {
    css: 'css',
    style: 'css/style.css'
}

//php 처리
gulp.task('php', function () {
    console.log('==php===');
    return gulp.src(src.html)
        .pipe(browser_sync.stream());
});

//js 처리
gulp.task('js', function () {
    console.log('==js==');
    return gulp.src(src.js)
        .pipe(browser_sync.stream());
})


//CSS 처리
gulp.task('css', function () {
    console.log('gulp-css');
    var plugin = [  //플러그인 등록
        postcss_import
        , autoprefixer
        , postcss_simple_vars
        , postcss_nested
        , cssnano
    ];
    // return gulp.src(src.style)
    return gulp.src(src.style)
        .pipe(sourcemaps.init())    //sourcemap 초기화
        .pipe(postcss(plugin))      //postcss 처리
        .on('error', function (errorInfo) { //에러 처리
            console.log(errorInfo.toString());
            this.emit(end);
        })
        .pipe(sourcemaps.write())   //sourcemap 기록
        .pipe(gulp.dest(dest.css)) //css 배포
});

gulp.task('style', gulp.series('css', function () {
    return gulp.src(dest.style)
        .pipe(browser_sync.stream());

}));


//watch 
gulp.task('watch', function () {
    browser_sync.init({         //browser-sync 초기화
        // server: {
        //     baseDir: "./"
        // }
        proxy: "http://localhost:8080/"
    })
    gulp.watch([src.html], gulp.series('php'));
    gulp.watch([src.css], gulp.series('style'));
    gulp.watch([src.js], gulp.series('js'));
});

//기본작업 등록
gulp.task('default', gulp.series('watch'));