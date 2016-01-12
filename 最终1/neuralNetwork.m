clear;
clc;
%训练样本
%Inum,Imoney,Onum;
[NUM1,TXT,RAW]=xlsread('训练样本');
Inum=NUM1(:,2)';
Imoney=NUM1(:,3)';
Onum=NUM1(:,4)';
A=[];
F=length(Onum);
for i=1:1:F
    if(Onum(i)<16)
        t=[];
        for j=1:1:4
            t=[t mod(Onum(i),2)];
            Onum(i)=fix(Onum(i)/2);
        end
        A=[A;t];
    else
        A=[A;1 1 1 1];
    end
end
A=A';
%归一化
IN=(Inum-min(Inum))/(max(Inum)-min(Inum));
IM=(Imoney-min(Imoney))/(max(Imoney)-min(Imoney));
input=[IN;IM];
%input
%output=(Onum-min(Onum))/(max(Onum)-min(Onum));
%数据输入
%EPOCHS=10000;
%GOAL=0.5;
%建立神经网络
net=newff(minmax(input),[10,4],{'logsig','purelin'},'traingdx');
net.trainparam.show = 50 ;
net.trainparam.epochs = 10000 ;
net.trainparam.goal = 0.1 ;
net.trainParam.lr = 0.01 ;
net=train(net,input,A);
%实际数据
%InumA,ImoneyA;
[NUM,TXT,RAW]=xlsread('Sheet3')
InumA=NUM(:,5)';
ImoneyA=NUM(:,2)';
InumA=(InumA-min(InumA))/(max(InumA)-min(InumA));
ImoneyA=(ImoneyA-min(ImoneyA))/(max(ImoneyA)-min(ImoneyA));
%input=[InumA;ImoneyA];
inputA=[InumA;ImoneyA];
Y=sim(net,inputA);
Z=[];
[L,R]=size(Y);
for i=1:1:R
    Z=[Z 1*Y(1,i)+2*Y(2,i)+4*Y(3,i)+8*Y(4,i)];
end
xlswrite('jieguo.xls',round(Z'));
%Y=ceil(Y*(max(Onum)-min(Onum))+min(Onum));
%xlswrite('jieguo.xls',Y');