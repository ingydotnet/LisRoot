(native-header "TCanvas.h")
(native-header "TF1.h")
(require '[c_interop :as c])
(c/load-types "root_types.edn")

(defmacro overload []
  (defn paren [s] (str "(" s ")"))
  (defn * [& args] (apply str (interpose "*" args)))
  (defn sin [arg] (str "sin" (paren arg)))
  (defn pow [x n] (str "pow" (paren (str x "," n))))
  (defn / [a b] (str a "/" (paren b)))
  nil)

(overload)

(defmacro make-expression [x r ns]
  (def pi 3.1415)

  (defn single [x r ns]
    (pow (/ (sin (* pi r x))
            (* pi r x))
         2))

  (defn nslit0 [x r ns]
    (pow (/ (sin (* pi ns x))
            (sin (* pi x)))
         2))

  (defn nslit [x r ns]
    (* (single x r ns) (nslit0 x r ns)))

  (nslit x r ns))

(def nslit-string (make-expression "x" 0.2 2))

(println nslit-string)
;;=> pow(sin(3.1415*0.2*x)/(3.1415*0.2*x),2)*pow(sin(3.1415*2*x)/(sin(3.1415*x)),2)

(def c ((c/new TCanvas)))

(c/add-type [:Classes TF1] [:B string string int int])
(def Fnslits ((c/new TF1 :B) "Fnslits" nslit-string -5 5))

(c/add-type [:Classes TF1 SetNpx] [:A null int])
((c/call TF1 SetNpx) Fnslits 500)

((c/call TF1 Draw) Fnslits)
((c/call TCanvas Print) c "nslits_native.pdf")

(c/add-type [:Classes TF1 Eval]
            [:A null double])

(def now1 (micros))
(def erg1 ((c/call TF1 Eval) Fnslits 0.4))
(println "Call once: " (- (micros) now1))

(c/add-type [:Classes TF1 GetX]
            [:A null double double double double int])

(def now (micros))
(def erg ((c/call TF1 GetX) Fnslits 3.6 -5.0 0.3 1.E-14 1000000000))
(println "Root-runtime-compile: " (- (micros) now))

(c/defnative "double cpp_nslit(double* x, double* par)"
  (nslit "x[0]" 0.2 2))

(def FastSlits ((c/new TF1 :native cpp_nslit) "Fnslit" "native" -5.001 5. 2))

(def now2 (micros))
(def erg2 ((c/call TF1 GetX) FastSlits 3.6 -5.0 0.3 1.E-14 1000000000))
(println "Native interop: " (- (micros) now2))

((c/call TF1 Draw) FastSlits)
((c/call TCanvas Print) c "nslits_fast.pdf")

(native-declare
  "double nslitfun(double* x, double* par){
  return pow(sin((3.1415*0.2*x[0]))/(3.1415*0.2*x[0]),2)*pow(sin((3.1415*2*x[0]))/sin((3.1415*x[0])),2);
}

double runit() {
  TF1 *Fnslit  = new TF1(\"Fnslit\",nslitfun,-5.001,5.,2);
  return Fnslit->GetX(3.6, -5.0, 0.3, 1.E-14, 1000000000);
}
")

(defn runitnow [] "__result = obj<number>(runit())")

(def now3 (micros))
(def erg3 (runitnow))
(println "Native: " (- (micros) now3))

;;Call once:  15 +-3
;;Root-runtime-compile:  125 +-10

;;Native interop:  40 +-5
;;Native:  40 +-5