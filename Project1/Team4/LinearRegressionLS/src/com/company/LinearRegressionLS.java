package com.company;

import org.ejml.alg.dense.linsol.svd.SolvePseudoInverseSvd;
import org.ejml.alg.dense.misc.TransposeAlgs;
import org.ejml.alg.dense.mult.MatrixMatrixMult;
import org.ejml.data.DenseMatrix64F;

public class LinearRegressionLS {
    private long[] numberOfMac;
    private long[] numberOfPeople;
    // coefficients where the final result will be collected
    private double[] coefficients={0,0};

    LinearRegressionLS(long[] numberOfMac, long[] numberOfPeople){
        this.numberOfMac=numberOfMac;
        this.numberOfPeople=numberOfPeople;
    //    A= new DenseMatrix64F(numberOfMac.length,2);
    }
    public double[] getCoefficients(){

        DenseMatrix64F matrixA =new DenseMatrix64F(numberOfMac.length,2);
        //contains the number of people values
        DenseMatrix64F matrixB= new DenseMatrix64F(numberOfPeople.length,1);
        //Contains transpose(A)*A
        DenseMatrix64F matrixATranA=new DenseMatrix64F(2,2);
        //Contains transpose(A)*B
        DenseMatrix64F matrixATranB=new DenseMatrix64F(2,1);
        //Contains Inverse(transpose(A)*A)
        DenseMatrix64F matrixATranAInv = new DenseMatrix64F(2,2);
        //Contains the final result
        DenseMatrix64F matrixCoefficients = new DenseMatrix64F(2,1);
        //Defining matrixA
        for(int i=0;i<numberOfMac.length;i++){
            matrixA.set(i,0,1);
            matrixA.set(i,1,numberOfMac[i]);
            matrixB.set(i,0,numberOfPeople[i]);
        }

        MatrixMatrixMult.mult_small(transpose(matrixA),matrixA,matrixATranA);
        MatrixMatrixMult.mult_small(transpose(matrixA),matrixB,matrixATranB);
        SolvePseudoInverseSvd pinv =new SolvePseudoInverseSvd();
        pinv.setA(matrixATranA);
        pinv.invert(matrixATranAInv);
        MatrixMatrixMult.mult_small(matrixATranAInv, matrixATranB, matrixCoefficients);
        //Converting the result to double[] format
        coefficients[0]=matrixCoefficients.get(0,0);
        coefficients[1]=matrixCoefficients.get(1,0);
        return coefficients;
    }
    //Simple function that computes the transpose
   private DenseMatrix64F transpose(DenseMatrix64F matrixA){
        DenseMatrix64F matrixATranspose =new DenseMatrix64F(matrixA.getNumCols(),matrixA.getNumRows());
        TransposeAlgs.standard(matrixA, matrixATranspose);
     //  System.out.println(matrixATranspose.toString());
        return  matrixATranspose;
    }

}
