function fizzbuzz() {
       for (let i = 1; i <=100; i++) {
           if (i % 3 == 0 && i % 5 == 0) {
               console.log("FizzBuzz");
           }
           else if (i % 3 == 0) {
               console.log("Fizz");
           }
           else if (i % 5 == 0) {
               console.log("Buzz");
           }
           else {
               console.log(i);
           }
       }
}

function takeMin() {
    let args = prompt("Please enter 2 numbers:", "");
    if (args != null) {
        args = args.split(" ");
    }
    if (args.length != 2) {
        console.log("I said TWO numbers, you fool!");
    }
    else {
        console.log(min(args[0], args[1]));
    }
}

function min(x, y) {
    return x < y ? x : y;
}