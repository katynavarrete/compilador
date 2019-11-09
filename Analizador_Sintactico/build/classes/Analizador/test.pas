Program ejemplo ;
var k:integer;
procedure p (n:integer; g:integer);
var i,h:integer;
    a:boolean;
begin
if n<2 then h:=g+n
else begin
h:=g;
p(n-1,h);
k:=h;
p(n-2,g)
end;
writeln(n);
writeln(g);
end;
begin
k:=0; p(3,k)
end.