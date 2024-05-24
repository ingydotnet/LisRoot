# YAMLScript as new Python for Data Science
## YAML is a data format
YAML is a data format. It is used in math to store algorithms, it is used in software engineering to store program code, in meteorology to store weather forecasts. In math they call it formula, in computing they call it syntax, in meteorology they call it, well, data. To us, it is all the same: YAML.

An example: a car driving at 20mph for 2 hours will drive 40 miles. This is because `40 = 20 * 2`, and for this calculation the official formula is `s = v * t`.

This formula can be stored, if one wishes, in YAML as `defn s(v, t): v * t`. Believe it or not, this is valid YAML. As a proof, let YAMLScript, the YAML compiler, transform that formula into the JSON data format: `{"defn s(v, t)":"v * t"}`. Call it formula or syntax or data, up to this point it is all the same to us.

## Execute the data
YAMLScript also delivers a data format called Clojure. There, our formula reads `(defn s [v t] (*_ v t))`. In Clojure lingo this is called an S-Expression. The cool thing is that Clojure can be executed. This means that the following YAML gives the number `40` as a result:

```
defn s(v, t): v * t
say: s(20, 2)
```

For the record, in JSON the above program reads `{"defn s(v, t)":"v * t", "say":"s(20, 2)"}`.

Writing programs in a data format and calling that expressions is no news in science. The very popular Mathematica(TM) package does that already for decades to extreme success. In that context, data transformation is called expression manipulation or "symbolic calculation". And as a reminiscence to a traditional concept, snippets of YAML data are sometimes also called YeS-Expressions.

## YAMLScript and Python

To compare YAMLScript with Python, we show a snippet of CERN’s Python binding for its ROOT library which was designed to handle the enormous amounts of data generated by the world’s largest particle accelerator.


Python snippet from  https://root.cern/manual/python :
```
class Linear:
    def __call__(self, arr, par):
        return par[0] + arr[0]*par[1]

l = Linear()
```

In this example, already at the very beginning, we read the keyword `class` and look no further because classes stand for object oriented programming. While being very versatile, this paradigm should not occupy any scientist as they are already wrestling with gazillion other involved concepts. Also, Python code is not a data structure, a fact that is a great pity for a data scientist.

The same example reads in YAMLScript as follows:

```
defn Linear():
  fn([x] [d k]): d + (k * x)

l =: Linear()
```

On the one hand this syntax looks familiar to Python. But on the other hand it is, we think, simpler because it only involves functions. The simplicity for scientists becomes especially clear when we compare the YAMLScript function with its age-old notation of mathematics on paper:

```
f: R1 x R2 -> R; ((x), (d,k)) -> d + kx
```

## Macros, the magic sauce for C++ interop

To call into CERN's ROOT library, we need C++ interop which is a thorny subject. While many languages have a good way to interop with the ANSI-C language, when it comes to C++ it gets hairy.

Here the code-as-data property comes to the rescue. For our interop we use Macros which are just functions that take data and return data that then in turn is executed. In a way, Macros are used for what was above called "expression manipulation". In Macros, data is transformed from and to S-Expressions.

As a prerequisite for our interop, we took the ROOT library and manually encoded the signatures of relevant classes and functions into one single data structure. This allows us, via Macros, to generate the necessary interop functions at compile time.

But the real speciality of our interop is that the C++ signature-data is not just used at compile time. The signature-data is also used at runtime to perform a trick called polymorphic dispatch which is mandatory when calling into C++, a feature many interop schemes grapple with.

## Functional and Immutable
In this post, the notions of "pure function" and "immutability" were left out deliberately. While we adhere to those concepts as well, here we focus on "code-as-data", which is completely distinct.