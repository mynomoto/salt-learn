(ns mynomoto.salt-learn
  (:require
    salt))

(comment
  (spit "Wire.tla" (salt/transpile "src/Wire.clj"))
  (spit "DieHard.tla" (salt/transpile "src/DieHard.clj"))
  (spit "Clock.tla" (salt/transpile "src/Clock.clj")))
