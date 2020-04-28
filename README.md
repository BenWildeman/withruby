# withRuby Jenkins pipeline plugin

Plugin for managing Ruby within Jenkins pipelines that uses [rbenv](https://github.com/rbenv/rbenv) under the hood. Initial work created by [pedrocesar-ti](https://github.com/pedrocesar-ti) which can be found here: https://github.com/pedrocesar-ti/Xenv-jenkins-lib

## Contents

* [Why use?](#why-use)
* [Installation](#installation)
    * [Dynamic Import](#dynamic-import)
    * [Global Shared Library](#global-shared-library)
* [Usage](#usage)

## Why use?

In some instances, it's not easy to manage ruby versions within Jenkins slaves, which is especially true with Mac slaves. As macOS comes with a system Ruby that's out of date, it's not easy to override this and have Jenkins pick it up. This plugin makes sure it uses rbenv when trying to use Ruby.

## Installation

Can install with the two following ways:

* [Dynamic Import](#dynamic-import)
* [Global shared library](#global-shared-library)

### Dynamic Import

You can dynamically import the library adding this snippet at the beginning of your `Jenkinsfile`:

```groovy
library identifier: 'withruby-lib', 
        retriever: modernSCM([$class: 'GitSCMSource', 
        remote: 'https://github.com/BenWildeman/withruby.git'])
```

### Global Shared Library

With the [Global Shared Libraries](https://jenkins.io/doc/book/pipeline/shared-libraries/#global-shared-libraries), you can import this module inside your `Jenkinsfile` with the following:

```groovy
@Library('withruby-lib') _
```

## Usage


### withRuby(version, method)
* `version` - specify which Ruby version to use. **Default** `2.6.6`
* `method` - specify whether to keep or delete the Ruby version after use. **Default** `keep`

```groovy
script {
    withRuby() {
        sh "ruby --version"
    }
}
```

```groovy
script {
    withRuby("2.7.1", "delete") {
        sh "ruby --version"
    }
}
```
