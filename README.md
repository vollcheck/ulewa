# ulewa - weather application written in re-frame for learning purposes

### running the application

0. populate `api-key` value in the `ulewa.config` namespace

1. run from your console:
``` shell
shadow-cljs watch app
```

2. connect emacs to the running instance
```
cider-connect-cljs
```
with a port `55555` and `shadow-cljs` `:app` profile.

and you're good to go!
