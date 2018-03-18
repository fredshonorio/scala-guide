---
title: Introduction
date: 2018-03-15
---

This document is a short guide in building backend services in Scala, using
functional programming (FP), although a lot of ideas are applicable for other languages and different domains.
It is the result of working for some years in a Java codebase, with a functional
mindset. As such, a lot of parallels will be drawn between Java and Scala.

The motivating design principle is __maintainability__, meaning, among other things:

- It should be easy to identify causes of errors
- It should be easy to identify the responsibilities of a piece of code, enabling:
    - refactoring
    - replacing
    - adding features without impacting existing ones
    - remove it once it becomes unnecessary
- It should be easy to work on a system without a lot of cognitive load, e.g:
    - After a period of onboarding, it should be easy for a new developer to
understand the structure of _every_ service written in this manner
    - A developer that has not working on a service for a long time can quickly
get up to speed
- It should be easy to execute parts of the code, with either fake or real data
and real, local or mocked dependencies

It is possible to build correct systems (i.e. systems that work as specified/expected)
that are not maintainable but that is obviously less desirable.
