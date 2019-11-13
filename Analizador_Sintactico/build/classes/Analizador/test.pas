program factorial;
    var a: integer;
    function fact(n:integer):integer;
        function m:integer;
            begin 
                m:=45;
            end;
        begin
             
            if n < 2 then
            begin
                writeln(m);
                fact := 1
            end    
            else 
                fact := fact(n-1)*n;
               
        end;
        Begin
            readln(a);
            writeln(fact(a));
        End.