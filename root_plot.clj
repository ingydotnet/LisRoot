(native-header "TCanvas.h")
(native-header "TF1.h")

(require '[c_interop :as c])

(c/m-load-types "malli1.edn")

(defn n-times-x [n]
  (fn [[x]] (* n x)))

(def pf1 ((c/new TF1) "f1" (n-times-x 2) -5.001 5.0 2))
(def pc1 ((c/new TCanvas :B) "c1" "Something" 0 0 800 600))

((c/call TF1 Draw) pf1)
((c/call TCanvas Print) pc1 "root_plot_1.pdf")

(c/m-add-type [:TF1] [:B :string :string :int :int])

(def pf2 ((c/new TF1 :B) "f2" "sin(x)" -5 5))
(def pc2 ((c/new TCanvas :B) "c2" "Something" 0 0 800 600))

((c/call TF1 Draw) pf2)
((c/call TCanvas Print) pc2 "c_interop_2.pdf")
