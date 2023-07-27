(native-header "ROOT.h")
(require '[c_interop :as cxx])

(defn Linear []
  (fn [[x] [par0 par1]]
    (+ par0 (* x par1))))

(def f ((cxx/> new TF1) "pyf1" (Linear) -1. 1. 2))

((cxx/> SetParameters TF1) f 5. 2.)

(def c (cxx/> new TCanvas))

((cxx/> Draw TF1) f)
((cxx/> Print TCanvas) c "python_comparison_1.pdf")

;; Example 2

(defn LinearB [k d]
  (fn [[x]]
    (+ d (* k x))))

(println ((LinearB 2.0 5.0) (list 1.0))) ;;comment => 7.0
(println ((LinearB 2.0 4.0) (list 1.0))) ;;comment => 6.0

((cxx/> Draw TF1)
 ((cxx/> new TF1) "pyf2" (LinearB 2. 5.) -1. 1. 2))

((cxx/> Print TCanvas) c "python_comparison_2.pdf")
