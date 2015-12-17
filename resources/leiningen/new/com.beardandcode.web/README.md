A web application that does something super awesome.

## Dependencies

  - [Leiningen 2+](http://leiningen.org)
  - PhantomJS 1.9.8 - Sadly Selenium doesn't yet support PhantomJS 2+, [PhantomJS 1.9.8 can be found here](https://bitbucket.org/ariya/phantomjs/downloads)
  - [sassc](http://github.com/sass/sassc) - Can be found in homebrew for OSX

## Try

Find your way to the directory where you checked out this project and execute the following:

```
$ lein repl

user=> (go)  ;; starts the example webapp on a random port
...
...
... c.beardandcode.components.web-server - Started web server on http://127.0.0.1:8080

user=> (open!)  ;; only works on OSX

```

If you are not on OS X, open the url found in your logs it will likely be different to the one above.