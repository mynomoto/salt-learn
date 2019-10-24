(ns mynomoto.salt-learn
  (:require
    salt))

(comment
  (spit "Tic.tla" (salt/transpile "src/Tic.clj"))
  (spit "Clock.tla" (salt/transpile "src/Clock.clj")))
