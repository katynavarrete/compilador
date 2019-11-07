Program Conductor_de_Prueba;
Var
  c,h,r: integer;
  r2:boolean;

procedure pro(p: integer);
Var
  k,j: integer;
 
begin
  r2:=true;
  r := 4;

end;

function fun(p: integer;b:boolean):integer;
begin
  r2:=true;
  r := 4;
pro(fun(-10,false));
fun:=fun(fun(fun(10,true),9=10),9<5);
fun:=fun(fun(fun(10-10+50*-9+25/4,8<9),true),false);

end;
function fun1(a,b:boolean):integer;
begin
  r2:=true;
  r := 4;
pro(fun(-10,false));
fun1:=fun(fun(fun(10,true),9=10),9<5);
fun1:=fun(fun(fun(10-10+50*-9+25/4,8<9),true),false);

end;

Begin
  read(c);

 c:= fun(5, (((c > -(5*4*r))) or (3<>5) and true or (r>2)));
pro(8978);
c:=fun1(9<8=true,false=(96<-87 or 97/8 <> 987));
  if c<(-8+10*-9/-9*10+fun(4-2,-7<>-9=true)) then
  begin
     read(c);
  end
  else
     write(0);

End.
