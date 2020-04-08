#!/usr/bin/env groovy
import groovy.transform.Field

@Field rbenvRoot = "${HOME}/.rbenv"
@Field rbenv = "${rbenvRoot}/bin/rbenv"

def withLock(Closure cl) {
    lock("rbenv:${env.NODE_NAME}") {
        cl()
    }
}

def call(version="2.6.6", method=null, cl) {
    print "Setting up Ruby version: ${version}"

    withLock {
        if (!fileExists("${rbenv}")) {
            print "Installing rbenv"
            sh """
            git clone https://github.com/rbenv/rbenv.git ${rbenvRoot}
            cd "${rbenvRoot}"
            src/configure --without-ssl && make -C src
            """
            sh "git clone https://github.com/rbenv/ruby-build.git ${rbenvRoot}/plugins/ruby-build"
        }

        if (!fileExists("${rbenvRoot}/versions/${version}/")) {
            print "Installing Ruby version: ${version}"
            withEnv(["PATH=${rbenvRoot}/bin/:$PATH"]) {
                sh "rbenv install ${version}"
            }
        }
    }

    // use the Ruby version the user specified
    withEnv(["PATH=${rbenvRoot}/shims:${rbenvRoot}/bin/:$PATH", "RBENV_SHELL=sh"]) {
        withLock {
            print "Switch to Ruby version: ${version}"
            sh "rbenv rehash && rbenv local ${version}"
        }
        cl()
    }

    if (method == "delete") {
        print "Removing Ruby version: ${version}"
        withEnv(["PATH=${rbenvRoot}/bin/:$PATH"]) {
            File directory = new File("${rbenvRoot}/versions/${version}")
            directory.deleteDir()
        }
    } 
}
