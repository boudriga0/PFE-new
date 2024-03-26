import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IReclamation } from '../reclamation.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../reclamation.test-samples';

import { ReclamationService, RestReclamation } from './reclamation.service';

const requireRestSample: RestReclamation = {
  ...sampleWithRequiredData,
  date: sampleWithRequiredData.date?.format(DATE_FORMAT),
};

describe('Reclamation Service', () => {
  let service: ReclamationService;
  let httpMock: HttpTestingController;
  let expectedResult: IReclamation | IReclamation[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ReclamationService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Reclamation', () => {
      const reclamation = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(reclamation).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Reclamation', () => {
      const reclamation = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(reclamation).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Reclamation', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Reclamation', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Reclamation', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addReclamationToCollectionIfMissing', () => {
      it('should add a Reclamation to an empty array', () => {
        const reclamation: IReclamation = sampleWithRequiredData;
        expectedResult = service.addReclamationToCollectionIfMissing([], reclamation);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(reclamation);
      });

      it('should not add a Reclamation to an array that contains it', () => {
        const reclamation: IReclamation = sampleWithRequiredData;
        const reclamationCollection: IReclamation[] = [
          {
            ...reclamation,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addReclamationToCollectionIfMissing(reclamationCollection, reclamation);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Reclamation to an array that doesn't contain it", () => {
        const reclamation: IReclamation = sampleWithRequiredData;
        const reclamationCollection: IReclamation[] = [sampleWithPartialData];
        expectedResult = service.addReclamationToCollectionIfMissing(reclamationCollection, reclamation);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(reclamation);
      });

      it('should add only unique Reclamation to an array', () => {
        const reclamationArray: IReclamation[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const reclamationCollection: IReclamation[] = [sampleWithRequiredData];
        expectedResult = service.addReclamationToCollectionIfMissing(reclamationCollection, ...reclamationArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const reclamation: IReclamation = sampleWithRequiredData;
        const reclamation2: IReclamation = sampleWithPartialData;
        expectedResult = service.addReclamationToCollectionIfMissing([], reclamation, reclamation2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(reclamation);
        expect(expectedResult).toContain(reclamation2);
      });

      it('should accept null and undefined values', () => {
        const reclamation: IReclamation = sampleWithRequiredData;
        expectedResult = service.addReclamationToCollectionIfMissing([], null, reclamation, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(reclamation);
      });

      it('should return initial array if no Reclamation is added', () => {
        const reclamationCollection: IReclamation[] = [sampleWithRequiredData];
        expectedResult = service.addReclamationToCollectionIfMissing(reclamationCollection, undefined, null);
        expect(expectedResult).toEqual(reclamationCollection);
      });
    });

    describe('compareReclamation', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareReclamation(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareReclamation(entity1, entity2);
        const compareResult2 = service.compareReclamation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareReclamation(entity1, entity2);
        const compareResult2 = service.compareReclamation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareReclamation(entity1, entity2);
        const compareResult2 = service.compareReclamation(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
