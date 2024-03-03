# Kushi quickstart
[Kushi](https://github.com/kushidesign/kushi) is a complete styling and UI solution for ClojureScript.

This template demonstrates how to setup a project with Kushi, using [shadow-cljs](https://github.com/thheller/shadow-cljs) and [Reagent](https://reagent-project.github.io/). You can find most of the documentation and examples that you need to get started via comments in the src files of this project.

This template is based on [shadow-cljs/browser-quickstart](https://github.com/shadow-cljs/quickstart-browser).

<br>

## Required Software

- [node.js (v6.0.0+)](https://nodejs.org/en/download/)
- [Java JDK (8+)](http://www.oracle.com/technetwork/java/javase/downloads/index.html) or [Open JDK (8+)](http://jdk.java.net/10/)


<br>

## Running the Example

```bash
git clone https://github.com/kushidesign/kushi-quickstart.git kushi-quickstart
cd kushi-quickstart
npm install
npx shadow-cljs watch app
```

This will begin the compilation of the configured `:app` build and re-compile whenever you change a file.

When you see a kushi report followed by a "Build completed." message (from shadow-cljs) your build is ready to be used.

```bash
[:app] Compiling ...
[:app] Using Kushi <version>
[:app] [Kushi <version>] - Writing 281 rules and 319 tokens.
[:app] Build completed. (176 files, 175 compiled, 0 warnings, 13.19s)
```

You can view this app at  [http://localhost:8020](http://localhost:8020).


The app is only a very basic skeleton. Please see [shadow-cljs/browser-quickstart](https://github.com/shadow-cljs/quickstart-browser) for more detailed info `shadow-cljs`-specific configuration options.

<br>

## Manual Setup Details
If you were to setup kushi manually, starting with the base [shadow-cljs template](https://github.com/shadow-cljs/quickstart-browser), you would follow these 3 steps:

#### 1) Add dependency, cache-blockers, and build hooks in `shadow-cljs.edn`
```Clojure
;; shadow-cljs configuration

{:source-paths
 ["src/dev"
  "src/main"
  "src/test"]

 :dependencies
 [[reagent "1.1.1"]
  [design.kushi/kushi "1.0.0-a.19"] ; ! Kushi dependency
  [binaryage/devtools "1.0.6"]]

 :dev-http
 {8020 "public"}

 :cache-blockers
 #{kushi.core kushi.stylesheet} ; ! Add these 2 namespaces to :cache-blockers entry.

 :builds
 {:app
  {:target
   :browser

   :build-hooks
   [(kushi.core/kushi-debug)
    (kushi.stylesheet/create-css-file)] ; ! Kushi build hooks

   :output-dir
   "public/js"

   :asset-path
   "/js"

   :modules
   {:main
    {:init-fn starter.browser/init}}}}}
```
<br>

#### 2) Create a `kushi.edn` at your project's root

```Clojure
;; This is a map with, at minimum, a `:css-dir` entry.
;; This must be relative to project root e.g "public/css" or "resources/public/css".
;; Please see the provided kushi.edn file for additional options.

{:css-dir "public/css"}
```
<br>

#### 3) Add the required `<link>` tag to your `index.html`
```html
<!-- The two tags with comments above them must be included for kushi to work. -->

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <!---
    The value of the `href` in the link tag below needs to
    align with the provided value in your kushi.edn config file.
    At minimum, you must specify a value for :css-dir in your kushi.edn.
    You can optionally specify a value for :css-filename.
    The default value for :css-filename will be `kushi.css`.
  -->
  <link rel="stylesheet" href="./css/kushi.css" type="text/css">
  <title>Kushi Quickstart</title>
</head>
<body>
  <noscript>You need to enable JavaScript to run this app.</noscript>
  <div id="app"></div>
  <script src="./js/main.js"></script>
</body>
</html>
```

<br>


At the very bottom of your app's main ns, you may want to include:
```Clojure
(when ^boolean js/goog.DEBUG
  (inject!))
```
This will inject the same stylesheet that kushi writes to disk into your browser, during development builds. You may not need or want to do this but if you are experiencing visual jankiness on reloads when developing, this can help.

<br>
Please see `src/main/starter/browser.cljs`, `src/main/starter/badges.cljs`, and `src/main/starter/shared_styles.cljs` for details on requiring and using various kushi functionality.
