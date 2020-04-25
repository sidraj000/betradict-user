#include<bits/stdc++.h>
using namespace std;
main()
{
    int a;
    cin>>a;
    for(int i=0;i<a;i++)
    {
        int k,ind;
        cin>>k>>ind;
        int arr[k];
        for(int j=0;j<k;j++)
        {
            cin>>arr[i];
        }
        sort(arr,arr+k);
       if((arr[k-1]-arr[0])%2==0)
       {
           if((arr[k-1]-arr[0])<=2*ind)
           cout<<(arr[k-1]+arr[0])/2;
           else cout<<-1;
       }
       else
       {
           if((arr[k-1]-arr[0]+1)<=2*ind)
           cout<<(arr[k-1]+arr[0])/2;
           else cout<<-1;
           
       }
       cout<<endl;
    }


    return 0;
}
   

