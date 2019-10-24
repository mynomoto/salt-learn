(ns mynomoto.salt-learn
  (:require
    salt))

(comment
  (spit "DieHard.tla" (salt/transpile "src/DieHard.clj"))
  (spit "Clock.tla" (salt/transpile "src/Clock.clj")))
