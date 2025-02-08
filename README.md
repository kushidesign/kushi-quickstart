# Kushi quickstart
[Kushi](https://github.com/kushidesign/kushi) is a complete styling and UI solution for ClojureScript.

This template demonstrates how to setup a project with Kushi, using [shadow-cljs](https://github.com/thheller/shadow-cljs) and [Reagent](https://reagent-project.github.io/). You can find most of the documentation and examples that you need to get started via comments in the source files of this project.

This template is based on [shadow-cljs/browser-quickstart](https://github.com/shadow-cljs/quickstart-browser)



<br>

## Required Software

- [Java JDK (8+)](http://www.oracle.com/technetwork/java/javase/downloads/index.html) or [Open JDK (8+)](http://jdk.java.net/10/)
- [node.js (v6.0.0+)](https://nodejs.org/en/download/)
- [Lightning CSS](https://lightningcss.dev/docs.html)


<br>

## Running the Example

```bash
git clone https://github.com/kushidesign/kushi-quickstart.git kushi-quickstart
cd kushi-quickstart
npm install
npx shadow-cljs watch app
```

This will begin the compilation of the configured `:app` build and re-compile whenever you change a file.

When you see a `"Build completed"` message (from `shadow-cljs`), your build is ready to be used.


View it at [http://localhost:8020](http://localhost:8020).

The app is only a very basic skeleton.

For more info on using Kushi UI components, check out the [kushi.design](https://kushi.design).

For detailed info on syntax and other features, check out the official [Kushi docs](https://github.com/kushidesign/kushi).

For more general info on `shadow-cljs`-specific configuration options, check out [shadow-cljs/browser-quickstart](https://github.com/shadow-cljs/quickstart-browser). 

<br>

## CSS size

By design, this quickstart will emit all the css which allows exploration of the design system, design tokens, semantic variants, and the entire library of pre-built components, even if you are just trying things out in your browser's dev tooling.

In a real-world project, you may very well be using only a subset of these things. In a shadow-cljs `release` build, the Kushi's CSS transpilation is designed to only include what you use in your code. 


<br>

## Manual Setup Details
If you were to setup Kushi manually, starting with the same base [shadow-cljs template](https://github.com/shadow-cljs/quickstart-browser), you would follow these 4 steps:

#### 1) &nbsp; Add dependency and Kushi build hook in `shadow-cljs.edn`
```Clojure
;; shadow-cljs configuration

{:source-paths
 ["src/dev"
  "src/main"
  "src/test"]

 :dependencies
 [[reagent "1.1.1"]
  [design.kushi/kushi "1.0.0-a.24"] ; ! Kushi dependency
  [binaryage/devtools "1.0.6"]]

 :dev-http
 {8020 "public"}

 :builds
 {:app
  {:target
   :browser

   :build-hooks
   [(kushi.css.build.analyze/hook)] ; ! Kushi build hooks

   :output-dir
   "public/js"

   :asset-path
   "/js"

   :modules
   {:main
    {:init-fn starter.browser/init}}}}}
```
<br>

#### 2) &nbsp; Create a `kushi.edn` at your project's root

```Clojure
;; This is a map with, at minimum, a `:css-dir` entry.
;; This must be relative to project root e.g "./public/css".
;; Please see the provided kushi.edn file for additional options.

{:css-dir "./public/css"}
```
<br>

#### 3) &nbsp; Add the required `<link>` tag to your `index.html`
Checkout the [`index.html`](https://github.com/kushidesign/kushi-quickstart/blob/main/public/index.html) file in this repo

<br>

#### 4) &nbsp; Install `lightningcss-cli` locally or globally
 Kushi uses [Lightning CSS](https://lightningcss.dev/) for bundling,
 minification, and syntax lowering for older browsers.

 Install locally
 ```
 npm install --save-dev lightningcss-cli
 ```
 Or, install globally
 ```
 npm install -g lightningcss-cli
 ```
