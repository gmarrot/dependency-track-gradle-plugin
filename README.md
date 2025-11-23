# Dependency-Track Gradle Plugin

![Build](https://github.com/gmarrot/dependency-track-gradle-plugin/actions/workflows/ci.yml/badge.svg?branch=main)
[![License: Apache 2](https://img.shields.io/badge/license-Apache%202-blue)](https://opensource.org/license/apache-2-0)

## Summary

TODO

## Requirements

This plugin requires Gradle 5.0 or higher to work as it uses APIs that are not available in lower versions.

## Quick Start

### Groovy

First, you need to apply the plugin in your `build.gradle`.

```groovy
plugins {
    id 'com.betomorrow.dependency-track' version '1.0.0'
}
```

Or you can use the legacy plugin declaration.

```groovy
buildscript {
    repositories {
        maven {
            url 'https://plugins.gradle.org/m2/'
        }
    }
    dependencies {
        classpath 'com.betomorrow.gradle:dependency-track-gradle-plugin:1.0.0'
    }
}

apply plugin: 'com.betomorrow.dependency-track'
```

### Kotlin

First, you need to apply the plugin in your `build.gradle.kts`.

```kotlin
plugins {
    id("com.betomorrow.dependency-track") version "1.0.0"
}
```

Or you can use the legacy plugin declaration.

```kotlin
buildscript {
    repositories {
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
    }
    dependencies {
        classpath("com.betomorrow.gradle:dependency-track-gradle-plugin:1.0.0")
    }
}

apply(plugin = "com.betomorrow.dependency-track")
```
