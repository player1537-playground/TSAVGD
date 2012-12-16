#!/usr/bin/python

from __future__ import print_function
import re
import sys

#s = "(if (< 4 5) (say \"true\") (say \"false\")"
s = sys.argv[1]
regexp = re.compile(r"([()])")
array = sum([filter(lambda x: x != "", re.split(regexp, a)) for a in re.findall(r'(?:"(?:\\"|[^"])+"|[^ ])+', s)], [])
#array = re.split('([()])', s)
print("Original: " + s)
print("  " + str(array))

output_file = open("Out.java", "w")

def fprint(s):
    output_file.write(str(s) + "\n")

class DataStreamer:
    def __init__(self, array):
        # Make a copy of the array so we don't change it later
        self.array = array[:]
    def peekNext(self):
        return self.array[0]
    def getNext(self):
        return self.array.pop(0)
    def nextIter(self):
        while self.notEmpty():
            yield self.getNext()
    def notEmpty(self):
        return len(self.array) > 0

class Identify:
    def __init__(self):
        ident = r"[-a-zA-Z_+*/%]"
        self.regIdentifier = re.compile(r"^"+ident+"+$")
        self.regSymbol = re.compile(r"^'"+ident+"+$")
        self.regString = re.compile(r'^"(?:\\\\"|[^"])+"$')
        self.regNumber = re.compile(r'^[-+]?[0-9.]+$')
    def isIdent(self, string):
        return re.match(self.regIdentifier, string) != None
    def isSymbol(self, string):
        return re.match(self.regSymbol, string) != None
    def isString(self, string):
        return re.match(self.regString, string) != None
    def isNumber(self, string):
        return re.match(self.regNumber, string) != None

class Token:
    def __init__(self, value_tuple):
        self.type, self.value = value_tuple
    def getValue(self):
        return self.value
    def getType(self):
        return self.type
    def __repr__(self):
        return "<%s: %s>" % (self.type, self.value)

class Parser:
    def __init__(self, array):
        self.data_s = DataStreamer(array)
        self.id = Identify()
    def function(self, funname, argslist):
        return Token(("FUNCALL", [funname] + argslist))
    def parse_sexpr(self):
        #print("At start of function: " + str(self.data_s.peekNext()))
        fun = None
        args = []
        while self.data_s.notEmpty():
            cur = self.data_s.getNext()
            #print("cur is: " + str(cur))
            if cur == '(':
                args.append(self.parse_sexpr())
            elif fun == None:
                fun = cur
            elif self.id.isSymbol(cur):
                # Remove the ' from the beginning
                args.append(Token(("SYMBOL", cur[1:])))
            elif self.id.isIdent(cur):
                args.append(Token(("IDENT", cur)))
            elif self.id.isString(cur):
                # Remove the quotes
                args.append(Token(("STRING", cur[1:-1])))
            elif self.id.isNumber(cur):
                args.append(Token(("NUMBER", cur)))
            elif cur == ')':
                return self.function(fun, args)
            else:
                print("What is this? " + str(cur))
        if fun == None:
            return args
        else:
            return self.function(fun, args)
        #print("how did I get here?" + str(fun))

class Compile:
    def __init__(self, sexpr_array):
        self.sexpr_array = sexpr_array
    def callFunction(self, funname, args):
        def paren(s):
            return "(%s)" % str(s)
        def oper(op, total=len(args)):
            return paren(op.join(args[:total]))
        funmap = {
            "+": lambda: op("+"),
            "-": lambda: op("-"),
            "/": lambda: op("/"),
            "*": lambda: op("*"),
            "<": lambda: op("<", 1),
            ">": lambda: op(">", 1),
            "=": lambda: op("==", 1),
            "!=": lambda: op("!=", 1),
            "not": lambda: "!" + paren(args[0]),
            "list": lambda: args,
            "say": lambda: "System.out.println(" + args[0] + ");",
            "begin": lambda: ";".join(args),
            "character": lambda: "new Person(%s) { void init() { setModel(%s); } void conversation() { %s } };" % (args[0], args[1], args[2]),
            "model": lambda: "new Model(%s)" % args[0],
            "set": lambda: "PropertyManager.setValue(%s%s)" % (args[0], "," + args[1] if len(args) > 1 else ""),
            "condition": lambda: "(PropertyManager.getValue(%s) != null && !PropertyManager.getValue(%s).equals(\"\"))" % (args[0], args[0]),
            }
        if funname in funmap:
            return funmap[funname]()
        else:
            return "%s(%s)" % (funname, ",".join(args))
    def getVariable(self, var):
        return "%s" % var
    def eval(self, sexpr=None):
        if sexpr == None:
            sexpr = self.sexpr_array
        if type(sexpr) is not list:
            print("Boxy!")
            sexpr = [sexpr]
        print("Called with " + str(sexpr))
        print(type(sexpr))
        for cur in sexpr:
            t = cur.getType()
            if t == "FUNCALL":
                # Make a copy of the sexpr value, an array of funname/args
                array = cur.getValue()[:]
                fun = array.pop(0)
                if fun == "if":
                    print(str(array))
                    return "if (%s) { %s } else { %s }" % (self.eval(sexpr=array[0]), self.eval(sexpr=array[1]), self.eval(sexpr=array[2]))
                elif fun == "conversation":
                    print("conversation:" + str(array))
                    ret = "Conversation.printConv(%s); " % self.eval(sexpr=array[0])
                    if len(array) > 1:
                        print("Got here")
                        ret += "Conversation.getResponse("
                        temp = [token.getValue() for token in array[1:]]
                        ret += ",".join([("new Response(%s, new Action() { void doAction() { %s; } })" % 
                                          (response, self.eval(sexpr=action)))
                                         for response, action in (temp[0], temp[1])])
                        ret += ");"
                    return ret
                else:
                    args = [self.eval(sexpr=sex) for sex in array]
                    return self.callFunction(fun, args)
            elif t == "IDENT":
                return self.getVariable(cur.getValue())
            elif t == "STRING" or t == "SYMBOL":
                return '"%s"' % cur.getValue()
            elif t == "NUMBER":
                return float(cur.getValue())

parser = Parser(array)
sexpr = parser.parse_sexpr()
print("sexpr is " + str(sexpr))
print("sexpr type is " + str(type(sexpr)))
compiler = Compile(sexpr)
fprint(compiler.eval())
#data_streamer = DataStreamer(array)
#for x in data_streamer.nextIter():
#    print ">> " + x
#    print "]] " + data_streamer.getNext()

