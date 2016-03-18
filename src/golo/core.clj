(ns golo.core
  (:require [thi.ng.geom.viz.core :as viz]
            [thi.ng.geom.svg.core :as svg]
            [thi.ng.geom.core.vector :as v]
            [thi.ng.color.core :as col]
            [thi.ng.math.core :as m :refer [PI TWO_PI]]))


(defn export-viz
  [spec path]
  (->> spec
       (viz/svg-plot2d-cartesian)
       (svg/svg {:width 600 :height 600})
       (svg/serialize)
       (spit path)))

(defn random-scalar ;scalar turned down for testing.
  "takes no arguments return random scalar."
  []
  (let [bool (rand-nth [true false])]
        (if bool 
          (rand-nth (range 1 100))
          (rand-nth (map #(/ % 100) (range 1 100))))))

(defmacro rand-op
  "returns un-evaluated symbol."
  []
  '(rand-nth ['+ '*]))

(defmacro rand-wave
  "returns unevaluated wave function."
  []
  '(list '* (random-scalar) (list (rand-nth ['Math/sin 'Math/cos]) (list '* (random-scalar) 'x))))

(defmacro function-generator
  "returns unevaluated wave series."
  []
  '(list 'fn '[x] (cons (rand-op) (repeatedly (rand-nth (range 1 6)) #(rand-wave)))))

(defn spec ;still need to find way to append function to graph as comment.
  "defines a graph spec by calling the function generator twice and graphing
  the resulting functions on an x-y plane."
  []
  (let [x-function (function-generator)
        y-function (function-generator)
        x-string (str x-function)
        y-string (str y-function)
        x-eval (eval x-function)
        y-eval (eval y-function)
        data-values (map (juxt x-eval y-eval) 
                         (range 0 300 0.01))
        xs (sort (map first data-values))
        ys (sort (map second data-values))
        x-domain [(first xs) (last xs)]
        y-domain [(first ys) (last ys)]]
    {:x-axis (viz/linear-axis
               {:domain x-domain
                :range  [50 590]
                :pos    -1})
      :y-axis (viz/linear-axis
                {:domain      y-domain 
                 :range       [550 20]
                 :pos         -1})
      :grid   {:attribs {:stroke "#caa"}
                :minor-x false
                :minor-y false}
      :data   [{:values data-values
                :attribs {:fill "#0af" :stroke "none"}
                :layout  viz/svg-scatter-plot}]}))

(defn -main
  "takes number of logos to generate and path."
  ([] (-main 1 ""))
  ([amount] (-main amount ""))
  ([amount path] 
   (map #(export-viz (spec) (str path "logo" % ".svg")) (range amount))))

